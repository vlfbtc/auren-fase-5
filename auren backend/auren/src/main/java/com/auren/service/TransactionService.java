package com.auren.service;

import com.auren.model.Transaction;
import com.auren.model.TransactionType;
import com.auren.model.User;
import com.auren.model.dto.TransactionRequest;
import com.auren.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;

    public List<Transaction> list(User user, LocalDate from, LocalDate to, Integer limit) {
        List<Transaction> list = txRepo.findByUserAndDateBetweenOrderByDateDesc(user, from, to);
        if (limit != null && limit > 0 && list.size() > limit) {
            return list.subList(0, limit);
        }
        return list;
    }

    public Transaction create(User user, TransactionRequest req) {
        // Parse do tipo vindo como String
        if (req.getType() == null) {
            throw new IllegalArgumentException("Tipo da transação é obrigatório (INCOME ou EXPENSE).");
        }
        TransactionType type = TransactionType.valueOf(req.getType().toUpperCase());

        // Categoria: se INCOME e vier nula, usamos um default “Renda”.
        String category = req.getCategory();
        if (type == TransactionType.INCOME && (category == null || category.isBlank())) {
            category = "Renda";
        }
        // Se EXPENSE e vier nula, podemos padronizar “Outros”.
        if (type == TransactionType.EXPENSE && (category == null || category.isBlank())) {
            category = "Outros";
        }

        Transaction tx = Transaction.builder()
                .user(user)
                .description(req.getDescription())
                .type(type) // sempre setar
                .amount(req.getAmount()) // manter positivo
                .date(req.getDate())
                .category(category)
                .build();

        return txRepo.save(tx);
    }

    public Transaction update(User user, Long id, TransactionRequest req) {
        Transaction tx = txRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        if (req.getType() == null) {
            throw new IllegalArgumentException("Tipo da transação é obrigatório (INCOME ou EXPENSE).");
        }
        TransactionType type = TransactionType.valueOf(req.getType().toUpperCase());

        String category = req.getCategory();
        if (type == TransactionType.INCOME && (category == null || category.isBlank())) {
            category = "Renda";
        }
        if (type == TransactionType.EXPENSE && (category == null || category.isBlank())) {
            category = "Outros";
        }

        tx.setDescription(req.getDescription());
        tx.setType(type);
        tx.setAmount(req.getAmount());
        tx.setDate(req.getDate());
        tx.setCategory(category);

        return txRepo.save(tx);
    }

    public void delete(User user, Long id) {
        Transaction tx = txRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
        txRepo.delete(tx);
    }

    // Helpers para insights (ex.: últimos 6 meses)
    public List<Transaction> lastMonths(User user, int months) {
        var all = txRepo.findTop180ByUserOrderByDateDesc(user);
        LocalDate cutoff = LocalDate.now().minusMonths(months).withDayOfMonth(1);
        return all.stream()
                .filter(t -> !t.getDate().isBefore(cutoff))
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .toList();
    }
}
