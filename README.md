# **Raft Consensus Implementation**:rowboat:

Consensus indicates that several servers agree on the same information, which is required for designing fault-tolerant distributed systems. 
Consensus algorithms enable a group of computers to function as a coherent entity that can withstand the failures of some of its members.
RAFT protocol helps in achieving consensus.

In this project I implemented the two major task involved in RAFT algorithms
a. Leader Election
b. Safe Log Replication

## Leader Election
As per RAFT algorithm each node in a cluster can exist in three states, namely Leader, Candidate and Follower.
The below flow diagram explains the transition of nodes into different states.

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture1.1.JPG">
</p>

For implementing succesful Leader Election, I incorporated below mentioned mechanism on each node

1.Heartbeats
2.ElectionTimeout
3.AppendRPC
4.RquestRPC


## Block diagram showing the implementation details

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture3.JPG">
</p>


Please check this file to see more information on the Leader Election Implementation

##Safe Log Replication
Implemented Safe Log Replication along with modified Leader Election algorithm to ensure only a candidate with complete log must get 
elected as leader. The below flow diagram explains the log replication process.

<p align="center">
<img src="https://github.com/akdev121/RaftImplementation/blob/74eff67e269e5f7b0894bb01498e41b62eccbe0d/ReadmeImages/Capture2.JPG">
</p>

Implementing of safe log replication involves implementation of following mechanism
1.Leader Completeness - Candidates with incomplete logs must not be elected as leader
2.Log replications to all the followers by the leader
3.Log ordering - Ensure the integrity of log
4.Log inconsistencies - Managing Follower's log with conflicting log entries or incomplete log entries

Please check this file to see more information on the Safe Log Replication Implementation

##How to use this repository
Please check Readme inside folder for using this repository

##Testing and Validation
please check this file for testing and valiation

##Tools and Technologies
Java
Java Spring
Multithreading in Java
UDP Datagram Packets - Used UDP packets for sending heartbeats, vote requests and other messages
Docker - Built 5 container to simulate algorithm


##References
1. https://docs.docker.com/storage/volumes/
2. https://docs.docker.com/engine/reference/builder/
3. https://docs.docker.com/compose/
4. https://docs.docker.com/compose/networking/
5. https://raft.github.io/#implementations
6. https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/
7. http://thushw.blogspot.com/2011/06/asynchronous-udp-server-using-java-nio.html