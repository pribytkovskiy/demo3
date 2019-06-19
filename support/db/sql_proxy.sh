cp ./cloud-sql-proxy.service /etc/systemd/system/cloud-sql-proxy.service
sudo systemctl daemon-reload
sudo systemctl start cloud-sql-proxy

#./cloud_sql_proxy -instances=dulcet-cat-242615:europe-west6:bike-championship=tcp:3306 -credential_file=dulcet-cat-242615-da3d51e311df.json
