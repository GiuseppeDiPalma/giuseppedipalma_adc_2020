# Project of Distributed Architectures for Cloud 2020

### Author: Giuseppe Di Palma

## Requisites

- ...
- ...

## Project Assignment

On this, I computed md5 of myfirstnamelastname-dayofbirth on: [md5online](https://www.md5online.org/md5-encrypt.html)

md5(giuseppedipalma-31) = ***8***185eaebad44a09266a9c2a66878bc46

***Value 5 = Project number 5 🤦‍♂️***

## Project Description

### 5 Semantic Harmony Social Network

Design and develop a social network based on the user’s interests that exploits a P2P Network. The system collects the profiles of the users and automatically creates friendships according to a matching strategy. The users can see their friends over time and are automatically informed when a user enters the social network and becomes a new potential friend. The system defines a set of questions, for instance, if the user like or not like a set of photos, a set of a hashtag, or more accurate as Big Five Personality Test.

At this point, the system can compute the user scoring according to the answers. This scoring is elaborated by a matching strategy that automatically finds out the friends. Consider, for instance, a binary answers vector; a matching process should be the difference in 0 and 1, or the Hamming distance, and so on. The system allows the users to see the social network questions, create a profile score according to the answer, join in the network using a nickname, and eventually see all user friends. As described in the [SemanticHarmonySocialNetwork](https://github.com/spagnuolocarmine/distributedsystems_class_2020/blob/master/homework/SemanticHarmonySocialNetwork.java) Java API.

## TO-DO LIST

- [ ] Create users;
  - [ ] Name, nickname;
  - [ ] List of friends, number of friends;
- [ ] Create profile of users;
- [ ] Create test with 5 questions;
- [ ] List of friends with similar answer;
  - [ ] Answer is 1 or 0 (big five personality); or other

## Usage

```bash
docker build --no-cache -t social-p2p .
```

```bash
docker run -i --name MASTER-PEER -e MASTERIP="127.0.0.1" -e ID=0 social-p2p
```
