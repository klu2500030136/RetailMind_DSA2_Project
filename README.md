# RetailMind – Smart Retail Inventory & Sales Analytics System

## Project Overview
RetailMind is a terminal-based Java application that completely digitizes modern retail operations. It serves as an academic capstone, comprehensively implementing advanced Data Structures and Algorithms (DSA) without relying on external databases.

## Objectives
Provide an extremely fast, computationally optimal backend architecture capable of managing inventory, executing sales, optimizing warehouse logistics, sorting vast amounts of data, and forecasting financial trends.

## Features
- **O(log n) Inventory Management** via AVL and B-Trees.
- **Fast Analytics** via Fenwick and Segment Trees.
- **Logistics Engine** calculating minimum spanning trees and shortest delivery routes.
- **Business Optimization Engine** identifying highest ROI products using DP and Greedy algorithms.
- **Automated Reporting Suite** providing full executive dashboards.

## DSA Coverage
- Trees: AVL, B-Tree, Segment Tree, Fenwick Tree
- Graphs: BFS, DFS, MST, Dijkstra, Bellman-Ford , Floyd-Warshall
- Sorting: Merge, Quick, Heap, Counting, Radix
- Optimization: Greedy (Activity Selection, Fractional Knapsack), Dynamic Programming (0/1 Knapsack, LIS)

## Folder Structure
```text
DSAEndProject/
├── src/retailmind/
│   ├── app/           (Main Entry)
│   ├── dsa/           (Data Structures Implementations)
│   ├── manager/       (Domain Managers)
│   ├── model/         (Entities)
│   ├── optimization/  (DP & Greedy logic)
│   ├── service/       (Analytics, Graph, Reports)
│   └── sorting/       (Sorting algorithms)
├── data/              (.txt persistent data)
├── reports/           (Auto-generated reports)
└── docs/              (UML, Flowcharts, Test Cases)
```

## Team Details
**Project No**: 11  
**Team Lead**: 2500031878 - CH. Naveen  
**Member 1**: 2500030720 - M. Lokesh  
**Member 2**: 2500032098 - B. Jeevan Sai  
**Member 3**: 2500030136 - G. Sai Teja  

## How to Compile
Ensure you have the JDK installed. From the root directory:
```bash
javac -d out src/retailmind/**/*.java
```

## How to Run
```bash
java -cp out retailmind.app.RetailMindApp
```

## Sample Output Screenshots Placeholder
[Screenshot: Main Menu]
[Screenshot: Generating Reports]

## Future Enhancements
- Machine Learning based demand prediction
- Web-based dashboard
- Database integration
- Cloud deployment
