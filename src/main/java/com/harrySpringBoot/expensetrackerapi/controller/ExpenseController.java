package com.harrySpringBoot.expensetrackerapi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.harrySpringBoot.expensetrackerapi.entity.Expense;
import com.harrySpringBoot.expensetrackerapi.service.ExpenseService;

import jakarta.validation.Valid;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses(Pageable page) {
        return expenseService.getAllExpenses(page).toList();
    }

    @GetMapping("/expenses/{id}")
    public Expense getExpenseById(@PathVariable("id") Long id) {
        return expenseService.getExpenseById(id);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/expenses")
    public void deleteExpenseById(@RequestParam("id") Long id) {
        expenseService.deleteExpenseById(id);
    }
    
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/expenses")
    public Expense addExpense(@Valid @RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    @PutMapping("/expenses/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Expense read = expenseService.updateExpense(id, expense);
        System.out.println(read);
        return read;
    }

    @GetMapping("/expenses/category")
    public List<Expense> getExpenseByCategory(@RequestParam String category,Pageable page) {
        return expenseService.readByCategory(category, page);
    }
    
    @GetMapping("/expenses/name")
    public List<Expense> getExpensesByName(@RequestParam String keyword, Pageable page) {
        return expenseService.readByName(keyword, page);
    }
    
    @GetMapping("/expenses/date")
    public List<Expense> getExpensesByDate(@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate,Pageable page ) {
        return expenseService.readByDate(startDate, endDate, page);
    }
    
    
}
