package javaxx;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.X509V3CertificateGenerator;

@SuppressWarnings("deprecation")
public class Cliente {

	private KeyPair key;
	private Socket echoSocket;
	private Estadistica estadistica;

	public Cliente(Estadistica est){

		estadistica = est;


		try{
			// Generación llave asimétrica
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			key = generator.generateKeyPair();

			//aqui esta la direccion y puerto que debe cambiar		
			echoSocket = new Socket("157.253.202.27", 8081);
			//echoSocket = new Socket("infracomp.virtual.uniandes.edu.co", 443);
			//	Socket echoSocket = new Socket("186.114.241.116", 80);   // sin seg
			echoSocket.setSoTimeout(10000);
			PrintStream out =
					new PrintStream(echoSocket.getOutputStream());

			PrintWriter out2 = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in =
					new BufferedReader(
							new InputStreamReader(echoSocket.getInputStream()));
			// saludo

			out.println("HOLA");
			String k = in.readLine();
			System.out.println(k);

			// Envio de algoritmos 

			out.println("ALGORITMOS:RC4:RSA:HMACMD5");

			k = in.readLine();

			System.out.println(k);

			// Envío de certificado

			if(!k.equals("ESTADO:OK")){

				throw new Exception("Al enviar los algoritmos , el servidor no responde correctamente :"+k);
			}



			out.println("CERCLNT");
			X509Certificate cert = generateCertificate(key);
			byte[] myByte = cert.getEncoded();
			echoSocket.getOutputStream().write(myByte);
			echoSocket.getOutputStream().flush();

			// Autenticación de servidor

			k  = in.readLine();

			System.out.println(k);

			if(!k.equals("CERTSRV")){
				throw new Exception("Al enviar el certificado , el servidor no esta respondiendo correctamente :"+k);
			}


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


			Cipher encripter = Cipher.getInstance("RC4");
			encripter.init(Cipher.ENCRYPT_MODE, llaveSimetricaServ);

			byte[] encriptedLocation = encripter.doFinal("2212,300.9090".getBytes());
			String hexaLocation = Hex.toHexString( encriptedLocation );


			// Se envía la primera actualización

			out2.println("ACT1:"+hexaLocation);


			// Se envía la segunda actualización

			Mac macCode = Mac.getInstance("HMACMD5");
			macCode.init(llaveSimetricaServ);
			byte[] locationBytes = macCode.doFinal("2212,300.9090".getBytes());

			encripter = Cipher.getInstance(llavePublicaSer.getAlgorithm());
			encripter.init(Cipher.ENCRYPT_MODE, llavePublicaSer);
			byte[] encriptedLocationMac =encripter.doFinal(locationBytes);
			String hexaLocation2 = Hex.toHexString( encriptedLocationMac );


			out2.println("ACT2:"+hexaLocation2);


			// Se lee la respuesta final

			k = in.readLine();

			System.out.println(k);



			echoSocket.close();

		}
		catch(Exception e){
			estadistica.aumentarConexionPerdida();
			e.printStackTrace();

			try {
				echoSocket.close();
			} catch (IOException e1) {

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


	public SecretKey extraerLlaveSim(String texto){

		try{

			Cipher cifrador = Cipher.getInstance("RSA");
			cifrador.init(Cipher.DECRYPT_MODE, key.getPrivate());

			byte[] bytesLlaveCifrada = DatatypeConverter.parseHexBinary(texto);
			byte[] bytesLlaveDescifrada = cifrador.doFinal(bytesLlaveCifrada);

			SecretKey simetricKey = new SecretKeySpec(bytesLlaveDescifrada,0,bytesLlaveDescifrada.length,"RC4");

			return simetricKey;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) {
		
		@SuppressWarnings("unused")
		Cliente c = new Cliente(new Estadistica());
	}

}
