package javaxx;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.x509.X509V3CertificateGenerator;

public class Cliente {

	private KeyPair key;
	private Socket echoSocket;

	public Cliente(){



		try{
			// Generación llave asimétrica
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			key = generator.generateKeyPair();


			//  Socket echoSocket = new Socket("186.114.241.116", 443);
		echoSocket = new Socket("infracomp.virtual.uniandes.edu.co", 443);
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

					
					PublicKey llavePublicaSer = serverCert.getPublicKey();

					// Se obtiene el mensaje con la llave cifrada

					k = in.readLine();

					System.out.println(k);

					String llaveStr = k.split(":")[1];
					
					SecretKey llaveSimetricaServ = this.extraerLlaveSim(llaveStr);
					
					System.out.println(llaveSimetricaServ);
					
					

					// Se inicia la actualización
					

					Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
					cipher.init(Cipher.ENCRYPT_MODE, llaveSimetricaServ);

					byte[] ubicacionCifrada = cipher.doFinal("4124.2028,210.4418".getBytes());

					
					// Se envía la primera actualización
					out.print("ACT1:");
					echoSocket.getOutputStream().write(ubicacionCifrada);
					echoSocket.getOutputStream().flush();


					// Se envía la segunda actualización
					out.print("ACT2:");
					echoSocket.getOutputStream().write(ubicacionCifrada);
					echoSocket.getOutputStream().flush();
					
					// Se lee la respuesta final
					
						k = in.readLine();

						System.out.println(k);
				}

			}
			echoSocket.close();

		}
		catch(Exception e){
			e.printStackTrace();

			try {
				echoSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
	
	
	public SecretKey extraerLlaveSim(String texto){

		try{

			Cipher cifrador = Cipher.getInstance("RSA");
			cifrador.init(Cipher.DECRYPT_MODE, key.getPrivate());

			byte[] bytesLlaveCifrada = DatatypeConverter.parseHexBinary(texto);
			byte[] bytesLlaveDescifrada = cifrador.doFinal(bytesLlaveCifrada);
			String simKey = new String(bytesLlaveDescifrada);

			System.out.println("Llave simetrica: " + simKey);
			SecretKey simetricKey = new SecretKeySpec(bytesLlaveDescifrada,0,bytesLlaveDescifrada.length,"AES");
			
			return simetricKey;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) {
		Cliente c = new Cliente();
	}

}
