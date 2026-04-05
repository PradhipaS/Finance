package com.finance.controller;

import com.finance.model.FinancialRecord;
import com.finance.service.FinancialRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final FinancialRecordService service;

    public RecordController(FinancialRecordService service) {
        this.service = service;
    }

    // POST /api/records
    // Body: { "amount": 5000, "type": "INCOME", "category": "Salary", "date": "2024-01-15", "notes": "Monthly salary" }
    @PostMapping
    public ResponseEntity<FinancialRecord> create(@RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        FinancialRecord.Type type = FinancialRecord.Type.valueOf(body.get("type").toUpperCase());
        String category = body.get("category");
        LocalDate date = LocalDate.parse(body.get("date"));
        String notes = body.get("notes");

        return ResponseEntity.ok(service.create(amount, type, category, date, notes));
    }

    // GET /api/records
    // Optional filters: ?type=INCOME or ?category=Salary or ?from=2024-01-01&to=2024-12-31
    @GetMapping
    public ResponseEntity<List<FinancialRecord>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(service.getAll(type, category, from, to));
    }

    // GET /api/records/{id}
    @GetMapping("/{id}")
    public ResponseEntity<FinancialRecord> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // PUT /api/records/{id}
    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecord> update(@PathVariable Long id,
                                                   @RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        FinancialRecord.Type type = FinancialRecord.Type.valueOf(body.get("type").toUpperCase());
        String category = body.get("category");
        LocalDate date = LocalDate.parse(body.get("date"));
        String notes = body.get("notes");

        return ResponseEntity.ok(service.update(id, amount, type, category, date, notes));
    }

    // DELETE /api/records/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Record deleted successfully"));
    }
}
