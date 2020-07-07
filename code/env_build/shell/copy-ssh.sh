#!/bin/bash
ssh-keygen -t rsa
for i in `cat /home/tiankx/slaves`
do
	echo -e "\033[31m ---------- $i ---------- \033[0m"
	ssh-copy-id $i
done