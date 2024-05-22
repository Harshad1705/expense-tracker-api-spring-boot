package com.harrySpringBoot.expensetrackerapi.service;


import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.harrySpringBoot.expensetrackerapi.entity.Expense;

public interface ExpenseService {

    Page<Expense> getAllExpenses(Pageable page);

    Expense getExpenseById(Long id);

    void deleteExpenseById(Long id);

    Expense addExpense(Expense expense);

    Expense updateExpense(Long id, Expense expense);
    
    List<Expense> readByCategory(String category, Pageable page);

    List<Expense> readByName(String keyword, Pageable page);

    List<Expense> readByDate(Date startDate, Date endDate, Pageable page);

}
