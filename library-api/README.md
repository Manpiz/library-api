1) Create borrower
POST /api/borrowers
Body: {"name":"Alice","email":"alice@example.com"}
Expect: 201 with borrower id.

2) Create a book copy
POST /api/books
Body: {"isbn":"9780000000001","title":"Clean Code","author":"Robert C. Martin"}
Expect: 201 with book id (each call creates a new copy).

3) List books
GET /api/books
Expect: 200 array; note the id from step 2.

4) Borrow that copy
POST /api/borrowers/{borrowerId}/borrowing
Body: {"bookId": <bookId>}
Expect: 201 with lend details (borrowedAt set).

5) Block double-borrow
Repeat step 4 with same bookId
Expect: 400 “Book is already borrowed”.

6) Return the copy
POST /api/borrowers/{borrowerId}/returns
Body: {"bookId": <bookId>}
Expect: 200 with returnedAt set.

7) Return again (no active lend)
Repeat step 6
Expect: 404 “Active lend not found...”.


Error/edge checks (optional)
Borrow with wrong borrowerId → 404.
Borrow with missing/invalid bookId → 404.
Create borrower with bad email → 400 validation errors.
Lightweight documentation (can paste into Postman “Documentation” or README):

## POST /api/borrowers — create borrower; body {name,email}; 201 returns borrower (id,name,email).
## POST /api/books — create book copy; body {isbn,title,author}; 201 returns book (id,isbn,title,author); rule: if ISBN exists, title/author must match.
## GET /api/books — list all book copies; 200 array.
## POST /api/borrowers/{borrowerId}/borrowing — borrow copy; body {bookId}; 201 returns lend (id, borrowerId, bookId, borrowedAt, returnedAt=null); rule: copy can be lent to only one borrower at a time.
## POST /api/borrowers/{borrowerId}/returns — return copy; body {bookId}; 200 returns lend with returnedAt set.
## Run order for quick sanity: 1 → 2 → 3 → 4 → 5 (expect 400) → 6 → 7 (expect 404).
