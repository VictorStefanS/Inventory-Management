# StoreFlow — Test Cases

Manual test cases covering input validation, boundary conditions, and application flow.

---

## Type Mismatch Tests

| ID | Input | Expected | Actual | Status                                             |
|----|-------|----------|--------|----------------------------------------------------|
| TC-01 | Integer passed in name field | Flash message: invalid input | Item was processed |  Acceptable — integers are valid characters in a name |
| TC-02 | String passed in quantity field | Flash message: invalid input | Browser prevents non-numeric input |  Pass — browser-level validation                   |
| TC-03 | Negative number in quantity field | Flash message: invalid input | Flash message: invalid input |  Pass                                              |
| TC-04 | Negative number in price field | Flash message: invalid input | Flash message: invalid input |  Pass                                              |
| TC-05 | Integer passed in price field | Flash message: invalid input | Browser prevents non-decimal input |  Pass — browser-level validation                   |

---

## Boundary Case Tests

| ID | Input | Expected | Actual | Status |
|----|-------|----------|--------|-------|
| TC-06 | String longer than 100 characters in name field | Flash message: invalid input | Item did not process, no flash message | 🐛 Bug — Fixed: added `name.length() > 100` check to `InventoryService` |
| TC-07 | Spaces only in name field | Flash message: invalid input | Browser blocks submission |  Pass — browser `required` attribute treats whitespace as empty |
| TC-08 | Special characters in name field | Flash message: invalid input | Item was processed |  Acceptable — special characters are valid in product names (e.g. "Anti-virus (3-pack)"). No SQL injection risk as JPA uses parameterised queries |
| TC-09 | Very large number in price field | Item is processed | Item was processed |  Pass |
| TC-10 | Very large number in quantity field | Item is processed | Flash message: invalid input |  Acceptable — integer overflow caught by GlobalExceptionHandler. Message could be more specific but no crash occurs |

---

## Flow Edge Case Tests

| ID | Input | Expected | Actual | Status |
|----|-------|----------|--------|-------|
| TC-11 | Selling more than available stock | Flash message: not enough stock | Flash message: not enough stock |  Pass |
| TC-12 | Selling from an item with 0 stock | Flash message: out of stock | Flash message: out of stock |  Pass |
| TC-13 | Updating price without entering a value | Flash message: invalid input | Flash message: invalid input |  Pass |
| TC-14 | Selling an item without entering quantity | Flash message: invalid input | Flash message: invalid input |  Pass |
| TC-15 | Searching without entering a value | Flash message: invalid input | Browser blocks submission |  Pass — browser `required` attribute |

---

## Bug Log

| ID | Description | Status | Fix Applied |
|----|-------------|--------|-------------|
| BUG-01 | Items with names longer than 100 characters were silently rejected with no user feedback | Fixed | Added `name.length() > 100` validation to `InventoryService.addItem()` — returns `INVALID_NAME` which triggers existing flash message in controller |

---

## Notes

- Browser-level validation (HTML `required` and `type="number"` attributes) acts as the first line of defence, blocking invalid input before it reaches the server
- Server-level validation in `InventoryService` acts as the second line of defence for business rule violations
- `GlobalExceptionHandler` acts as a safety net for any unexpected exceptions, preventing white error screens from reaching the user
- JPA parameterised queries protect against SQL injection automatically — special characters in input fields are not a security risk
