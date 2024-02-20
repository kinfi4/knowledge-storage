## Simple Ingress tutorial.

I've created two simple nginx services and an ingress to route traffic to them.
There's a simple `ingress.yaml` file that creates the ingress and routes traffic to the services 
    and deploy-ingress.sh that deploys the nginx ingress controller.

We can just `kubectl apply` to run app1 and app2 yaml configs, and `kubectl apply -f ingress.yaml` to create the ingress.
Then `bash deploy-ingress.sh` to deploy the nginx ingress controller.


Our kubernetes will provide our ingress with external IP which will be accessible from outside the cluster. 
We can use `kubectl get ingress` to get the external IP and use it to access the services.

```bash
NAME            CLASS   HOSTS                                       ADDRESS     PORTS   AGE
nginx-ingress   nginx   service1.example.com,service2.example.com   127.0.0.1   80      5m55s
```

Then we can update our `/etc/hosts` file to point `service1.example.com` and `service2.example.com` to the external IP.
(On windows it's `C:\Windows\System32\drivers\etc\hosts`)


### And that's it! We can now access our services using the domain names we've set up.
Just type `service1.example.com` or `service2.example.com` in your browser and you should see the nginx welcome page.
