# 📦 Product Service API

Base URL: `http://localhost:5555`

The service exposes two sets of endpoints:
- 🟣 **JPA-based** (`/products/jpa`)
- 🟢 **JDBC-based** (`/products/jdbc`)

---

## 🟣 JPA Controller (`/products/jpa`)

### 1. Create a product
```bash
curl -X POST http://localhost:5555/products/jpa \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 1299.99
  }'
```

**Response:**

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1299.99
}
```

### 2. Get product by ID

```bash
curl -X GET http://localhost:5555/products/jpa/1
```

**Response:**

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1299.99
}
```

### 3. Get all products

```bash
curl -X GET http://localhost:5555/products/jpa
```

### 4. Update a product

```bash
curl -X PUT http://localhost:5555/products/jpa/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "price": 1499.99
  }'
```

### 5. Delete a product

```bash
curl -X DELETE http://localhost:5555/products/jpa/1
```

---

## 🟢 JDBC Controller (`/products/jdbc`)

### 1. Create a product

```bash
curl -X POST http://localhost:5555/products/jdbc \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Phone",
    "price": 699.99
  }'
```

**Response:**

```json
{
  "id": 2,
  "name": "Phone",
  "price": 699.99
}
```

### 2. Get product by ID

```bash
curl -X GET http://localhost:5555/products/jdbc/2
```

**Response:**

```json
{
  "id": 2,
  "name": "Phone",
  "price": 699.99
}
```

### 3. Get all products

```bash
curl -X GET http://localhost:5555/products/jdbc
```

### 4. Update a product

```bash
curl -X PUT http://localhost:5555/products/jdbc/2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "price": 799.99
  }'
```

### 5. Delete a product

```bash
curl -X DELETE http://localhost:5555/products/jdbc/2
```
