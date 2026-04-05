package com.finance.service;

import com.finance.model.FinancialRecord;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {

    private final FinancialRecordRepository repository;

    public DashboardService(FinancialRecordRepository repository) {
        this.repository = repository;
    }

    // Total income, expenses and net balance
    public Map<String, Object> getSummary() {
        BigDecimal totalIncome = repository.sumByType(FinancialRecord.Type.INCOME);
        BigDecimal totalExpenses = repository.sumByType(FinancialRecord.Type.EXPENSE);

        // Handle nulls (when no records exist)
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpenses);
        summary.put("netBalance", netBalance);
        return summary;
    }

    // Total amount per category
    public Map<String, BigDecimal> getCategoryTotals() {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        List<Object[]> rows = repository.sumByCategory();
        for (Object[] row : rows) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            result.put(category, total);
        }
        return result;
    }

    // Monthly income and expense trends
    public List<Map<String, Object>> getMonthlyTrends() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Object[]> rows = repository.monthlyTrends();
        for (Object[] row : rows) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("month", row[0]);
            entry.put("type", row[1]);
            entry.put("total", row[2]);
            result.add(entry);
        }
        return result;
    }

    // Last 10 transactions
    public List<FinancialRecord> getRecentActivity() {
        return repository.findTop10ByDeletedFalseOrderByDateDesc();
    }
}
