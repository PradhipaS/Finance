package com.finance.service;

import com.finance.model.FinancialRecord;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialRecordService {

    private final FinancialRecordRepository repository;

    public FinancialRecordService(FinancialRecordRepository repository) {
        this.repository = repository;
    }

    // Create a new record
    public FinancialRecord create(BigDecimal amount, FinancialRecord.Type type,
                                   String category, LocalDate date, String notes) {
        FinancialRecord record = new FinancialRecord();
        record.setAmount(amount);
        record.setType(type);
        record.setCategory(category);
        record.setDate(date);
        record.setNotes(notes);
        return repository.save(record);
    }

    // Get all records with optional filters
    public List<FinancialRecord> getAll(String type, String category,
                                         LocalDate from, LocalDate to) {
        if (type != null && !type.isEmpty()) {
            return repository.findByDeletedFalseAndType(
                    FinancialRecord.Type.valueOf(type.toUpperCase()));
        }
        if (category != null && !category.isEmpty()) {
            return repository.findByDeletedFalseAndCategory(category);
        }
        if (from != null && to != null) {
            return repository.findByDeletedFalseAndDateBetween(from, to);
        }
        return repository.findByDeletedFalse();
    }

    // Get single record by id
    public FinancialRecord getById(Long id) {
        return repository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
    }

    // Update existing record
    public FinancialRecord update(Long id, BigDecimal amount, FinancialRecord.Type type,
                                   String category, LocalDate date, String notes) {
        FinancialRecord record = getById(id);
        record.setAmount(amount);
        record.setType(type);
        record.setCategory(category);
        record.setDate(date);
        record.setNotes(notes);
        return repository.save(record);
    }

    // Soft delete - just marks as deleted, does not remove from DB
    public void delete(Long id) {
        FinancialRecord record = getById(id);
        record.setDeleted(true);
        repository.save(record);
    }
}
