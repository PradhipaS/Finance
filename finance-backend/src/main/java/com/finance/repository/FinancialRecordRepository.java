package com.finance.repository;

import com.finance.model.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Get all non-deleted records
    List<FinancialRecord> findByDeletedFalse();

    // Filter by type
    List<FinancialRecord> findByDeletedFalseAndType(FinancialRecord.Type type);

    // Filter by category
    List<FinancialRecord> findByDeletedFalseAndCategory(String category);

    // Filter by date range
    List<FinancialRecord> findByDeletedFalseAndDateBetween(LocalDate from, LocalDate to);

    // Total income or expense
    @Query("SELECT SUM(r.amount) FROM FinancialRecord r WHERE r.type = :type AND r.deleted = false")
    BigDecimal sumByType(@Param("type") FinancialRecord.Type type);

    // Total per category
    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r WHERE r.deleted = false GROUP BY r.category")
    List<Object[]> sumByCategory();

    // Monthly trends
    @Query("SELECT DATE_FORMAT(r.date, '%Y-%m'), r.type, SUM(r.amount) " +
           "FROM FinancialRecord r WHERE r.deleted = false " +
           "GROUP BY DATE_FORMAT(r.date, '%Y-%m'), r.type " +
           "ORDER BY DATE_FORMAT(r.date, '%Y-%m')")
    List<Object[]> monthlyTrends();

    // Recent 10 records
    List<FinancialRecord> findTop10ByDeletedFalseOrderByDateDesc();
}
