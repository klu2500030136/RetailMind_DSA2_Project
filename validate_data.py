import os

def validate_datasets():
    errors = []
    
    # 1. Products Validation
    with open("data/products.txt", "r") as f:
        products_lines = f.read().splitlines()
    
    product_ids = set()
    product_names = set()
    barcodes = set()
    
    for idx, line in enumerate(products_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 6:
            errors.append(f"Products line {idx+1} malformed: {line}")
            continue
        pid, name, cat, price, stock, bc = parts
        
        if pid in product_ids: errors.append(f"Duplicate product ID: {pid}")
        product_ids.add(pid)
        
        if name in product_names: errors.append(f"Duplicate product name: {name}")
        product_names.add(name)
        
        if bc in barcodes: errors.append(f"Duplicate barcode: {bc}")
        barcodes.add(bc)
        
        try:
            if float(price) <= 0: errors.append(f"Price non-positive: {price}")
            if int(stock) < 0: errors.append(f"Stock negative: {stock}")
        except ValueError:
            errors.append(f"Invalid price/stock format in products: {line}")

    # 2. Customers Validation
    with open("data/customers.txt", "r") as f:
        customers_lines = f.read().splitlines()
        
    customer_ids = set()
    for idx, line in enumerate(customers_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 3:
            errors.append(f"Customers line {idx+1} malformed: {line}")
            continue
        cid, name, contact = parts
        
        if cid in customer_ids: errors.append(f"Duplicate customer ID: {cid}")
        customer_ids.add(cid)

    # 3. Suppliers Validation
    with open("data/suppliers.txt", "r") as f:
        suppliers_lines = f.read().splitlines()
        
    supplier_ids = set()
    for idx, line in enumerate(suppliers_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 3:
            errors.append(f"Suppliers line {idx+1} malformed: {line}")
            continue
        sid, name, loc = parts
        
        if sid in supplier_ids: errors.append(f"Duplicate supplier ID: {sid}")
        supplier_ids.add(sid)

    # 4. Transactions Validation
    with open("data/transactions.txt", "r") as f:
        transactions_lines = f.read().splitlines()
        
    for idx, line in enumerate(transactions_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 6:
            errors.append(f"Transactions line {idx+1} malformed: {line}")
            continue
        tid, cid, pid, qty, total, date = parts
        
        if cid not in customer_ids: errors.append(f"Tx uses invalid Customer ID: {cid}")
        if pid not in product_ids: errors.append(f"Tx uses invalid Product ID: {pid}")
        try:
            if int(qty) <= 0: errors.append(f"Tx qty non-positive: {qty}")
        except ValueError:
            errors.append(f"Invalid qty format in Tx: {qty}")

    # 5. Sales Validation
    with open("data/sales.txt", "r") as f:
        sales_lines = f.read().splitlines()
        
    for idx, line in enumerate(sales_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 2:
            errors.append(f"Sales line {idx+1} malformed: {line}")
            continue
        day, rev = parts
        try:
            if float(rev) <= 0: errors.append(f"Sales revenue non-positive: {rev}")
        except ValueError:
            errors.append(f"Invalid sales format: {rev}")

    # 6. Network Validation
    with open("data/network.txt", "r") as f:
        network_lines = f.read().splitlines()
        
    for idx, line in enumerate(network_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 3:
            errors.append(f"Network line {idx+1} malformed: {line}")
            continue
        src, dest, cost = parts
        try:
            if float(cost) <= 0: errors.append(f"Network edge cost non-positive: {cost}")
        except ValueError:
            errors.append(f"Invalid network edge cost: {cost}")

    # 7. Promotions Validation
    with open("data/promotions.txt", "r") as f:
        promo_lines = f.read().splitlines()
        
    for idx, line in enumerate(promo_lines):
        if not line.strip(): continue
        parts = line.split(',')
        if len(parts) != 3:
            errors.append(f"Promotions line {idx+1} malformed: {line}")
            continue
        name, start, end = parts
        try:
            if int(start) >= int(end): errors.append(f"Promotion start >= end: {line}")
        except ValueError:
            errors.append(f"Invalid promotion format: {line}")

    if errors:
        print("Validation errors found:")
        for err in errors: print("-", err)
    else:
        print("All validations passed.")

if __name__ == "__main__":
    validate_datasets()
