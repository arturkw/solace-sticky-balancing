# Solace Sticky Balancing

This example shows how to preserve sticky balancing of four nodes connected to Solase JMS broker.

# Setup
Pull Docker image and runt it:

```ssh
sudo docker pull solace/solace-pubsub-standard
sudo docker run -d -p 8080:8080 -p 55555:55555 --shm-size=1g --env username_admin_globalaccesslevel=admin --env username_admin_password=admin solace/solace-pubsub-standard
```

Connect to Solace CI and create:
 - Solace VPN with name defaultvpn
 - user with credentials *default* / *default*
 - queues QUEUE1, QUEUE2, QUEUE3, QUEUE4,
 - topic BALANCING_TOPIC
