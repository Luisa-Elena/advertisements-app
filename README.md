## Database

![Image](https://github.com/user-attachments/assets/5d146914-dafe-4d0e-b42a-60adecc5bf94)

### IDEA  
Create a separate table for storing specific fields for each type. Each concrete `Ad` has a foreign key (`ad_id`) referencing the base `Ad` table, which stores common fields for all ads.

### PROS  
- **Strong typing**: All fields are defined explicitly in SQL with types and constraints.  
- **Schema clarity**: The database schema clearly communicates what data exists for each ad type.  
- **Relational integrity**: You get full use of relational constraints (e.g., `NOT NULL`, `FOREIGN KEY`, `CHECK`, etc.).  
- **Powerful querying**: Complex queries are easier to optimize, and joins are predictable.

### CONS  
- **Rigid**: Adding a new ad type means:  
  - Creating a new table.  
  - Updating queries like `getAll()` and `save()`.  
  - Modifying the application code (Queries in the AdRepository class must be updated and implicitly the methods from this class too).  
- **Joins overhead**: Every fetch requires joins between `ad` and the corresponding subtype table.  
- **Code branching**: Backend logic must switch on ad type for inserts, updates, or reads.

### Ideal for  
- Applications with a **limited and well-defined set of ad types**, where the scenario of adding a new ad type is very unlikely to happen.  
- Systems where data consistency is critical.
