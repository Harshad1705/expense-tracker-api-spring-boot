package com.harrySpringBoot.expensetrackerapi.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harrySpringBoot.expensetrackerapi.entity.Expense;





@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    // select * from tbl_expense where userId = ? and category=?
    Page<Expense> findByUserIdAndCategory(Long userId,String category, Pageable page);
    

    // select * from tbl_expenses where userId=? and name LIKE '%keyword%'
    Page<Expense> findByUserIdAndNameContaining(Long userId , String keyword, Pageable page);

    
    // select * from tbl_expenses where userId=? and date between 'startDate' and 'endDate'
    Page<Expense> findByUserIdAndDateBetween(Long userId , Date startDate, Date endDate, Pageable page);

    // select * from tbl_expenses where id = userId;
    Page<Expense> findByUserId(Long userId, Pageable page);
    
    
    // select * from tbl_expenses where userId = userId and id = expenseId;
    Optional<Expense> findByUserIdAndId(Long userId, Long expenseId);
    
    
}
