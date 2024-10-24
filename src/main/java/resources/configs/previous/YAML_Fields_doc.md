# CloudSim YAML Configuration Format

## Global Fields
- **costPerSec**: `float`  
  Cost per second for using the datacenter's CPU.

- **costPerMem**: `float`  
  Cost per memory usage (per MB).

- **costPerStorage**: `float`  
  Cost per storage usage (per MB).

- **costPerBw**: `float`  
  Cost per bandwidth usage (per Mbps).

## Hosts Section
Defines the physical machines in the data center.

- **size**: `list<int>` (size: 1)  
  Number of hosts.

- **heterogeneous**: `boolean`  
  If `true`, host parameters vary; if `false`, they are homogeneous.

- **ram**: `list<int>` (size: 1 or 2)  
  RAM capacity (in MB) for each host. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **storage**: `list<int>` (size: 1 or 2)  
  Storage capacity (in MB) for each host. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **bw**: `list<int>` (size: 1 or 2)  
  Bandwidth (in Mbps) for each host. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **pesNumber**: `list<int>` (size: 1 or 2)  
  Number of processing elements (PEs) for each host. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **mips**: `list<int>` (size: 1 or 2)  
  MIPS rating (Million Instructions Per Second) for each PE in the host. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **vmScheduler**: `int`  
  VM scheduling policy:
  - `0`: TimeShared
  - `1`: SpaceShared

## VMs Section
Defines the virtual machines deployed on the hosts.

- **size**: `list<int>` (size: 1)  
  Number of VMs.

- **heterogeneous**: `boolean`  
  If `true`, VM parameters vary; if `false`, they are homogeneous.

- **ram**: `list<int>` (size: 1 or 2)  
  RAM capacity (in MB) for each VM. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **storage**: `list<int>` (size: 1 or 2)  
  Storage capacity (in MB) for each VM. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **bw**: `list<int>` (size: 1 or 2)  
  Bandwidth (in Mbps) for each VM. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **pesNumber**: `list<int>` (size: 1 or 2)  
  Number of PEs for each VM. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **mips**: `list<int>` (size: 1 or 2)  
  MIPS rating for each PE in the VM. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **taskScheduler**: `int`  
  Task scheduling policy for the VMs:
  - `0`: TimeShared
  - `1`: SpaceShared
  - `2`: CompletelyFair

## Tasks Section
Defines the cloudlets (tasks) that will be run on the VMs.

- **size**: `list<int>` (size: 1 or 2)  
  Number of tasks. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **heterogeneous**: `boolean`  
  If `true`, task parameters vary; if `false`, they are homogeneous.

- **fileSize**: `list<int>` (size: 1 or 2)  
  Input file size (in MB) for each task. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **outputSize**: `list<int>` (size: 1 or 2)  
  Output size (in MB) for each task. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **pesNumber**: `list<int>` (size: 1 or 2)  
  Number of PEs required for each task. 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

- **length**: `list<int>` (size: 1 or 2)  
  Computational complexity of the task, specified as Million Instructions (MI). 
  - If size is 1, it uses that fixed value. 
  - If size is 2, a random value is generated between the first and second values (inclusive).

## Configuration Example

```
    costPerSec: 0.05
    costPerMem: 0.01
    costPerStorage: 0.001
    costPerBw: 0.002

    hosts:
    - size: [5]
        heterogeneous: true
        ram: [8192, 16384]         # Random value between 8192 and 16384 MB
        storage: [50000, 100000]   # Random value between 50000 and 100000 MB
        bw: [2000]                 # Fixed value: 2000 Mbps
        pesNumber: [4, 8]          # Random value between 4 and 8 PEs
        mips: [10000, 20000]       # Random value between 10000 and 20000 MIPS
        vmScheduler: 1             # SpaceShared

    vms:
    - size: [10]
        heterogeneous: false
        ram: [2048]                # Fixed value: 2048 MB
        storage: [10000, 20000]    # Random value between 10000 and 20000 MB
        bw: [1000]                 # Fixed value: 1000 Mbps
        pesNumber: [2]             # Fixed value: 2 PEs
        mips: [500]                # Fixed value: 500 MIPS
        taskScheduler: 0           # TimeShared
    - size: [5]
        heterogeneous: true
        ram: [1024, 4096]          # Random value between 1024 and 4096 MB
        storage: [5000, 10000]      # Random value between 5000 and 10000 MB
        bw: [500]                  # Fixed value: 500 Mbps
        pesNumber: [1, 2]          # Random value between 1 and 2 PEs
        mips: [250, 750]           # Random value between 250 and 750 MIPS
        taskScheduler: 2           # CompletelyFair

    tasks:
    size: [100, 500]             # Random value between 100 and 500 tasks
    heterogeneous: false
    fileSize: [1000]             # Fixed value: 1000 MB
    outputSize: [500, 1500]      # Random value between 500 and 1500 MB
    pesNumber: [1, 4]            # Random value between 1 and 4 PEs
    length: [10000, 20000]       # Random value between 10000 and 20000 MI
```

## Scenarios

Scenarios to compare Broker polices: RoundRobin - Simulated Annealing -PSO
| Scenario | VM's Number | Number of Cloudlets |
|----------|-------------|---------------------|
| 1        | 1           | 25                  |
| 2        | 1           | 50                  |
| 3        | 1           | 100                 |
| 4        | 1           | 150                 |
| 5        | 1           | 200                 |
| 6        | 3           | 25                  |
| 7        | 3           | 50                  |
| 8        | 3           | 100                 |
| 9        | 3           | 150                 |
| 10       | 3           | 200                 |
| 11       | 6           | 25                  |
| 12       | 6           | 50                  |
| 13       | 6           | 100                 |
| 14       | 6           | 150                 |
| 15       | 6           | 200                 |
| 16       | 9           | 25                  |
| 17       | 9           | 50                  |
| 18       | 9           | 100                 |
| 19       | 9           | 150                 |
| 20       | 9           | 200                 |
| 21       | 15          | 25                  |
| 22       | 15          | 50                  |
| 23       | 15          | 100                 |
| 24       | 15          | 150                 |
| 25       | 15          | 200                 |

