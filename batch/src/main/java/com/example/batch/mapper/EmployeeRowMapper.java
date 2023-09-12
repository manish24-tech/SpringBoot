package com.example.batch.mapper;

import com.example.batch.model.Employee;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(RowSet rowSet) throws Exception {
        // Assuming the columns are in order: name, number, email

        return null;
    }
}
