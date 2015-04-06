package javaxx;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

import sun.security.x509.*;

public class Cliente {

	private KeyPair key;

	public Cliente(){



		try{
			// Generación llave asimétrica
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			key = generator.generateKeyPair();


			//  Socket echoSocket = new Socket("186.114.241.116", 443);
			Socket echoSocket = new Socket("infracomp.virtual.uniandes.edu.co", 443);
			//	Socket echoSocket = new Socket("186.114.241.116", 80);   // sin seg
			PrintStream out =
					new PrintStream(echoSocket.getOutputStream());
			BufferedReader in =
					new BufferedReader(
							new InputStreamReader(echoSocket.getInputStream()));
			// saludo
			out.println("HOLA");
			String k = in.readLine();
			System.out.println(k);

			// Envio de algoritmos   no esta respondiendo
			out.println("ALGORITMOS:DES:RSA:HMACMD5");

			k = in.readLine();

			System.out.println(k);

			// Envío de certificado

			if(k.equals("ESTADO:OK")){




				out.println("CERCLNT");
				X509Certificate cert = generateCertificate(key);
				byte[] myByte = cert.getEncoded();
				echoSocket.getOutputStream().write(myByte);
				echoSocket.getOutputStream().flush();

				// Autenticación de servidor

				k  = in.readLine();

				System.out.println(k);

				if(k.equals("CERTSRV")){




					// Se obtiene el certificado del servidor

					//					int tamBuffer = echoSocket.getReceiveBufferSize();
					byte[] bytesRecibidos = new byte[520];
					echoSocket.getInputStream().read(bytesRecibidos, 0, 520);
					CertificateFactory certificateCreator = CertificateFactory.getInstance("X.509");
					InputStream inputCert = new ByteArrayInputStream(bytesRecibidos);
					X509Certificate serverCert = (X509Certificate)certificateCreator.generateCertificate(inputCert);


					// Se obtiene el mensaje con la llave cifrada

					k = in.readLine();

					System.out.println(k);

					String llaveStr = k.split(":")[1];

					String llaveFull = this.descifrar(llaveStr.getBytes(), key);

					System.out.println("La llave es : ");


					// Se inicia la actualización
					if(k.equals("INIT")){

						out.println("ACT1");

						out.println("ACT2");

						k = in.readLine();

						System.out.println(k);


					}

				}






			}
			echoSocket.close();

		}
		catch(Exception e){
			e.printStackTrace();


		}



	}

	public X509Certificate generateCertificate(KeyPair pair)
			throws GeneralSecurityException, IOException
	{
		X509V3CertificateGenerator generatorMax = new X509V3CertificateGenerator();

		generatorMax.setSerialNumber(BigInteger.valueOf(1020788794));


		generatorMax.setNotBefore(new Date(System.currentTimeMillis() - 2678400000L));
		generatorMax.setNotAfter(new Date(System.currentTimeMillis() + 2678400000L));


		generatorMax.setIssuerDN(new X500Principal("CN=HernandezMarioINC"));


		generatorMax.setSubjectDN(new X500Principal("CN=HernandezMarioINC"));
		generatorMax.setPublicKey(pair.getPublic());


		generatorMax.setSignatureAlgorithm("SHA256WITHRSA");


		X509Certificate certificado = generatorMax.generate(pair.getPrivate());
		return certificado;
	}   

	public String descifrar(byte[] cipheredText, KeyPair key) {

		String res = "";
		try{
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
			byte [] clearText = cipher.doFinal(cipheredText);
			res = new String(clearText);

		}catch(Exception e ){

			e.printStackTrace();
		}
		return res;
	}


	public static void main(String[] args) {
		Cliente c = new Cliente();
	}

}
