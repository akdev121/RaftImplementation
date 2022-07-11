# **Raft Consensus Implementation**:rowboat:

Consensus indicates that several servers agree on the same information, which is required for designing **fault-tolerant distributed systems**. 
Consensus algorithms enable a group of computers to function as a coherent entity that can withstand the failures of some of its members.
**RAFT protocol helps in achieving consensus.**

In this project I implemented the two major task involved in RAFT algorithms
* **Leader Election**
* **Safe Log Replication**

## Leader Election
As per RAFT algorithm each node in a cluster can exist in three states, namely **Leader, Candidate and Follower**.
The below flow diagram explains the transition of nodes into different states.

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture1.1.JPG">
</p>

For implementing succesful Leader Election, I incorporated below mentioned mechanism on each node

* Heartbeats
* ElectionTimeout
* AppendRPC
* RquestRPC


## Block diagram showing the implementation details

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture3.JPG">
</p>


Please check this [file](https://github.com/akdev121/RaftImplementation/blob/9b0b2ec1847b24316bcb2a13a47a3cc24a20e982/Report/Implementation.pdf) to see more information on the Leader Election Implementation

## Safe Log Replication
Implemented Safe Log Replication along with modified Leader Election algorithm to ensure only a candidate with complete log must get 
elected as leader. The below flow diagram explains the log replication process.

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture2.JPG">
</p>

Implementing of safe log replication involves implementation of following mechanism
* **Leader Completeness** - Candidates with incomplete logs must not be elected as leader.
* **Log replications** to all the followers by the leader.
* **Log ordering** - Ensure the integrity of log.
* **Log inconsistencies** - Managing Follower's log with conflicting log entries or incomplete log entries.

Please check this [file](https://github.com/akdev121/RaftImplementation/blob/9b0b2ec1847b24316bcb2a13a47a3cc24a20e982/Report/Implementation.pdf) to see more information on the Safe Log Replication Implementation

## Tools and Technologies
* **Java 8**
* **Java Spring**
* **Multithreading in Java**
* **UDP Datagram Packets** - Used UDP packets for sending heartbeats, vote requests and other messages
* **Docker** - Built 5 container to simulate algorithm

## How to use this repository
* Please check Readme.pdf [here](https://github.com/akdev121/RaftImplementation/blob/9b0b2ec1847b24316bcb2a13a47a3cc24a20e982/Readme/Readme.pdf)

## Testing and Validation
* please check validation.pdf [here](https://github.com/akdev121/RaftImplementation/blob/9b0b2ec1847b24316bcb2a13a47a3cc24a20e982/Report/Validation.pdf) 

## Demo Video
Please check the demo video of the implementation [here](https://github.com/akdev121/RaftImplementation/tree/master/DemoVideo)


## References
* https://docs.docker.com/storage/volumes/
* https://docs.docker.com/engine/reference/builder/
* https://docs.docker.com/compose/
* https://docs.docker.com/compose/networking/
* https://raft.github.io/#implementations
* https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/
* http://thushw.blogspot.com/2011/06/asynchronous-udp-server-using-java-nio.html