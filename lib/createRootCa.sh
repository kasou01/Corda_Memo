#!/usr/bin/bash
root_keystore_keypair_password="changeme2"
ca_keystore_keypair_password="changeme"

keytool -genkeypair -keyalg EC -keysize 256 -alias ca -dname "CN=Root CA, OU=Cordite Foundation Network, O=Cordite Foundation, L=London, ST=London, C=GB" -ext bc:ca:true,pathlen:1 -ext bc:c -ext eku=serverAuth,clientAuth,anyExtendedKeyUsage -ext ku=digitalSignature,keyCertSign,cRLSign -keystore ca.jks -storepass ${ca_keystore_keypair_password} -keypass ${ca_keystore_keypair_password} 
keytool -exportcert -rfc -alias ca -keystore ca.jks -storepass ${ca_keystore_keypair_password} -keypass ${ca_keystore_keypair_password} > ca.pem
keytool -genkeypair -keyalg EC -keysize 256 -alias cert -dname "CN=Root CA, OU=Cordite Foundation Network, O=Cordite Foundation, L=London, ST=London, C=GB" -ext bc:ca:true,pathlen:1 -ext bc:c -ext eku=serverAuth,clientAuth,anyExtendedKeyUsage -ext ku=digitalSignature,keyCertSign,cRLSign -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} -validity 3650
keytool -genkeypair -keyalg EC -keysize 256 -alias key -dname "CN=Root CA, OU=Cordite Foundation Network, O=Cordite Foundation, L=London, ST=London, C=GB" -ext bc:ca:true,pathlen:1 -ext bc:c -ext eku=serverAuth,clientAuth,anyExtendedKeyUsage -ext ku=digitalSignature,keyCertSign,cRLSign -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} -validity 3650
keytool -certreq -alias cert -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} | keytool -gencert -ext eku=serverAuth,clientAuth,anyExtendedKeyUsage -ext bc:ca:true,pathlen:1 -ext bc:c -ext ku=digitalSignature,keyCertSign,cRLSign -rfc -keystore ca.jks -alias ca -storepass ${ca_keystore_keypair_password} -keypass ${ca_keystore_keypair_password} > cert.pem
keytool -certreq -alias key -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} | keytool -gencert -ext eku=serverAuth,clientAuth,anyExtendedKeyUsage -ext bc:ca:true,pathlen:1 -ext bc:c -ext ku=digitalSignature,keyCertSign,cRLSign -rfc -keystore ca.jks -alias ca -storepass ${ca_keystore_keypair_password} -keypass ${ca_keystore_keypair_password} > key.pem
keytool -importcert -noprompt -file cert.pem -alias cert -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} 2>/dev/null
keytool -importcert -noprompt -file key.pem -alias key -keystore root.jks -storepass ${root_keystore_keypair_password} -keypass ${root_keystore_keypair_password} 2>/dev/null
mv root.jks old.jks
keytool -importkeystore -srcstorepass ${root_keystore_keypair_password} -srckeystore old.jks -deststorepass ${root_keystore_keypair_password} -destkeystore old.p12 -deststoretype pkcs12 

openssl pkcs12 -in old.p12 -out pemfile.pem -nodes -passin pass:${root_keystore_keypair_password} -passout pass:${root_keystore_keypair_password}

openssl pkcs12 -export -in pemfile.pem -name cert -out cert.p12 -passin pass:${root_keystore_keypair_password} -passout pass:${root_keystore_keypair_password}

openssl pkcs12 -export -in pemfile.pem -name key -out key.p12 -passin pass:${root_keystore_keypair_password} -passout pass:${root_keystore_keypair_password}

keytool -importkeystore -srcstorepass ${root_keystore_keypair_password} -srckeystore cert.p12 -deststorepass ${root_keystore_keypair_password} -destkeystore root.jks -srcstoretype pkcs12 

keytool -importkeystore -srcstorepass ${root_keystore_keypair_password} -srckeystore key.p12 -deststorepass ${root_keystore_keypair_password} -destkeystore root.jks -srcstoretype pkcs12 

exit 0