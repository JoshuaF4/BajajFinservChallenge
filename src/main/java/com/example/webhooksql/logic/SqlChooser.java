package com.example.webhooksql.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SqlChooser {

    private final String regNo;

    public SqlChooser(@Value("${app.candidate.regNo}") String regNo) {
        this.regNo = regNo;
    }

    private static String onlyDigits(String s) {
        return s.replaceAll("[^0-9]", "");
    }

    private static int lastTwo(String digits) {
        if (digits.isEmpty()) return 0;
        String tail = digits.length() >= 2 ? digits.substring(digits.length()-2) : digits;
        return Integer.parseInt(tail);
    }

    public String choose() {
        int n = lastTwo(onlyDigits(regNo));
        boolean even = (n % 2 == 0);
        if (even) {
            return evenQuestionSql();
        } else {
            return oddQuestionPlaceholder();
        }
    }

    private String evenQuestionSql() {
        return "SELECT\n" +
               "  e.EMP_ID,\n" +
               "  e.FIRST_NAME,\n" +
               "  e.LAST_NAME,\n" +
               "  d.DEPARTMENT_NAME,\n" +
               "  COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT\n" +
               "FROM EMPLOYEE e\n" +
               "JOIN DEPARTMENT d\n" +
               "  ON d.DEPARTMENT_ID = e.DEPARTMENT\n" +
               "LEFT JOIN EMPLOYEE e2\n" +
               "  ON e2.DEPARTMENT = e.DEPARTMENT\n" +
               " AND e2.DOB > e.DOB\n" +
               "GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME\n" +
               "ORDER BY e.EMP_ID DESC;";
    }

    private String oddQuestionPlaceholder() {
        return "-- TODO: Insert final SQL for Question 1 (odd regNo).";
    }
}
