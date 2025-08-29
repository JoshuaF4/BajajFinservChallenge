# Webhook SQL Solver — Spring Boot

This is a headless **Spring Boot application** that:

1. On startup, sends a `POST` request to the **generateWebhook** API with candidate details.  
2. Receives a `webhook` URL and an `accessToken` (JWT).  
3. Selects the SQL query based on the last two digits of the registration number.  
   - **Even RegNo → Question 2** (implemented here for `22BIT0082`)  
   - **Odd RegNo → Question 1** (placeholder in code, can be filled if needed)  
4. Saves the SQL query to `build/solution-final.sql`.  
5. Submits the final SQL query to the `testWebhook` URL using the JWT token in the **Authorization** header (token used as-is, no `Bearer ` prefix).  

---

## Candidate Details
- **Name**: Joshua Daniel Rajan  
- **Reg No**: 22BIT0082  
- **Email**: joshuadaniel.rajan2022@vitstudent.ac.in  

---

## Final SQL Query (Question 2 — Even RegNo)

```sql
SELECT
  e.EMP_ID,
  e.FIRST_NAME,
  e.LAST_NAME,
  d.DEPARTMENT_NAME,
  COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e
JOIN DEPARTMENT d
  ON d.DEPARTMENT_ID = e.DEPARTMENT
LEFT JOIN EMPLOYEE e2
  ON e2.DEPARTMENT = e.DEPARTMENT
 AND e2.DOB > e.DOB
GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e.EMP_ID DESC;
