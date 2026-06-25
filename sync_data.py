import os

def create_files():
    # 1. products.txt
    products = [
        "101,Laptop,Electronics,55000.0,10,BC101",
        "102,Mouse,Accessories,500.0,50,BC102",
        "103,Keyboard,Accessories,1200.0,25,BC103",
        "104,Monitor,Electronics,18000.0,8,BC104",
        "105,Smartphone,Electronics,49999.0,22,BC105",
        "106,Smart Watch,Electronics,10000.0,32,BC106",
        "107,Headphones,Accessories,2500.0,40,BC107",
        "108,USB Hub,Accessories,800.0,60,BC108",
        "109,Webcam,Electronics,3500.0,15,BC109",
        "110,Printer,Electronics,12000.0,5,BC110",
        "111,SSD Drive,Storage,4500.0,30,BC111",
        "112,Pen Drive 64GB,Storage,700.0,80,BC112",
        "113,Router,Networking,2800.0,20,BC113",
        "114,Speaker,Accessories,1800.0,35,BC114",
        "115,External HDD 1TB,Storage,5500.0,18,BC115"
    ]
    with open("data/products.txt", "w") as f:
        f.write("\n".join(products))

    # 2. customers.txt
    customers = [
        "1,John Doe,9876543210",
        "2,Alice Smith,9123456780",
        "3,Ravi Kumar,9988776655",
        "4,Priya Patel,9012345678",
        "5,Sai Teja,9876001234",
        "6,Meena Reddy,9345678901",
        "7,Arjun Rao,9900112233",
        "8,Divya Nair,9654321098",
        "9,Kiran Babu,9871234567",
        "10,Sunita Joshi,9223344556"
    ]
    with open("data/customers.txt", "w") as f:
        f.write("\n".join(customers))

    # 3. suppliers.txt
    suppliers = [
        "1,Tech Distributors,Hyderabad",
        "2,Digital Hub,Bangalore",
        "3,Gadget World,Mumbai",
        "4,Smart Supply Co.,Chennai",
        "5,Future Electronics,Delhi",
        "6,Prime Parts,Pune",
        "7,Electro Mart,Kolkata",
        "8,GreenTech Suppliers,Ahmedabad"
    ]
    # Wait, the graph network requires 'SupplierA' to 'SupplierH' based on PDF Section 11.4.
    # To keep network.txt and suppliers.txt consistent (if they share the same ID logic in code),
    # Let's check how GraphNetwork and SupplierManager are used.
    # The requirement explicitly states "SupplierA", "SupplierB" etc. as nodes.
    # Let's write suppliers as they are in the table, and we'll check consistency.
    with open("data/suppliers.txt", "w") as f:
        f.write("\n".join(suppliers))

    # 4. transactions.txt
    # TxID,CustID,ProdID,Qty,Total,Date
    transactions = [
        "T01,1,101,2,110000.0,2026-06-25",
        "T02,2,102,5,2500.0,2026-06-25",
        "T03,3,105,1,49999.0,2026-06-25",
        "T04,4,103,3,3600.0,2026-06-25",
        "T05,5,106,2,20000.0,2026-06-25",
        "T06,1,107,1,2500.0,2026-06-25",
        "T07,6,108,4,3200.0,2026-06-25",
        "T08,7,109,1,3500.0,2026-06-25",
        "T09,8,111,2,9000.0,2026-06-25",
        "T10,9,112,3,2100.0,2026-06-25",
        "T11,10,113,1,2800.0,2026-06-25",
        "T12,2,114,2,3600.0,2026-06-25",
        "T13,3,115,1,5500.0,2026-06-25",
        "T14,4,104,1,18000.0,2026-06-25",
        "T15,5,110,1,12000.0,2026-06-25",
        "T16,6,101,1,55000.0,2026-06-25",
        "T17,7,105,2,99998.0,2026-06-25",
        "T18,8,102,10,5000.0,2026-06-25",
        "T19,9,107,3,7500.0,2026-06-25",
        "T20,10,106,1,10000.0,2026-06-25"
    ]
    with open("data/transactions.txt", "w") as f:
        f.write("\n".join(transactions))

    # 5. sales.txt (30 days)
    sales = [
        "1,5000.0", "2,6200.0", "3,8000.0", "4,7500.0", "5,9200.0",
        "6,6100.0", "7,11000.0", "8,9800.0", "9,7300.0", "10,58699.0",
        "11,13500.0", "12,11800.0", "13,15000.0", "14,12300.0", "15,17800.0",
        "16,14500.0", "17,16000.0", "18,13200.0", "19,18500.0", "20,20000.0",
        "21,16200.0", "22,14900.0", "23,19500.0", "24,21000.0", "25,18700.0",
        "26,22300.0", "27,20100.0", "28,25000.0", "29,23400.0", "30,27800.0"
    ]
    with open("data/sales.txt", "w") as f:
        f.write("\n".join(sales))

    # 6. network.txt
    network = [
        "SupplierA,SupplierB,50.0",
        "SupplierA,SupplierC,30.0",
        "SupplierB,SupplierD,20.0",
        "SupplierC,SupplierD,25.0",
        "SupplierC,SupplierE,40.0",
        "SupplierD,SupplierF,15.0",
        "SupplierE,SupplierG,35.0",
        "SupplierF,SupplierH,10.0",
        "SupplierG,SupplierH,20.0"
    ]
    with open("data/network.txt", "w") as f:
        f.write("\n".join(network))

    # 7. promotions.txt
    promotions = [
        "Diwali Sale,1,3",
        "Weekend Deal,2,4",
        "Flash Sale,5,6",
        "Year End Sale,7,10",
        "New Year Offer,11,13",
        "Republic Day Sale,14,16",
        "Summer Sale,17,20",
        "Monsoon Offer,21,24",
        "Independence Sale,25,27",
        "Clearance Sale,28,30"
    ]
    with open("data/promotions.txt", "w") as f:
        f.write("\n".join(promotions))

if __name__ == "__main__":
    create_files()
    print("Files created successfully.")
