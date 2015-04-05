package javaxx;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.security.cert.X509Certificate;
import java.util.Date;

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
			PrintWriter out =
					new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in =
					new BufferedReader(
							new InputStreamReader(echoSocket.getInputStream()));
			// saludo
			out.println("HOLA");
			String k = in.readLine();
			System.out.println(k);

			// Envio de algoritmos   no esta respondiendo
			out.print("ALGORITMOS:RSA:AES:DSA");

			k = in.readLine();

			System.out.println(k);

			// Envío de certificado

			if(k.equals("ESTADO:OK")){




				out.println("CERCLNT");
				X509Certificate cert = generateCertificate("CN=Test, L=London, C=GB",key,1,"SHA1withRSA");
				byte[] myByte = cert.getEncoded();
				echoSocket.getOutputStream().write(myByte);
				echoSocket.getOutputStream().flush();

				// Autenticación de servidor

				k  = in.readLine();

				System.out.println(k);

				if(k.equals("CERTSRV")){


					// se obtiene de alguna manera

					// y se valida de alguna manera




					k = in.readLine();

					System.out.println(k);

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

	public X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
			throws GeneralSecurityException, IOException
	{
		PrivateKey privkey = pair.getPrivate();
		X509CertInfo info = new X509CertInfo();
		Date from = new Date();
		Date to = new Date(from.getTime() + days * 86400000l);
		CertificateValidity interval = new CertificateValidity(from, to);
		BigInteger sn = new BigInteger(64, new SecureRandom());
		X500Name owner = new X500Name(dn);

		info.set(X509CertInfo.VALIDITY, interval);
		info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
		info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
		info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
		info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
		info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
		AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
		info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

		// Sign the cert to identify the algorithm that's used.
		X509CertImpl cert = new X509CertImpl(info);
		cert.sign(privkey, algorithm);

		// Update the algorith, and resign.
		algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
		info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
		cert = new X509CertImpl(info);
		cert.sign(privkey, algorithm);
		return cert;
	}   

	//	public byte[] calcular() { try {
	//		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORTIMO);
	//		generator.initialize(1024);
	//		keyPair = generator.generateKeyPair(); PrivateKey priv = keyPair.getPrivate(); PublicKey pub = keyPair.getPublic(); System.out.println(pub);
	//		￼￼
	//		Signature firma = Signature.getInstance(priv.getAlgorithm()); firma.initSign(priv);
	//		FileInputStream arch = new FileInputStream(ARCHIVO); BufferedInputStream bufin = new BufferedInputStream(arch); byte [] buffer = new byte[1024];
	//		int len;
	//		while (bufin.available() != 0) {
	//		len = bufin.read(buffer);
	//		firma.update(buffer,0,len); }
	//		bufin.close();
	//		byte [] signature = firma.sign(); String s1 = new String(signature); System.out.println("Firma: " + s1);
	//		return signature; }
	//		catch (Exception e) {
	//		System.out.println("Excepcion: " + e.getMessage()); return null;
	//		} }

	//	public void verificar(byte[] firma) { try {
	//		PublicKey pub = key.getPublic();
	//		Signature sig = Signature.getInstance(pub.getAlgorithm()); sig.initVerify(pub);
	//		FileInputStream arch = new FileInputStream(ARCHIVO); BufferedInputStream bufin = new BufferedInputStream(arch); byte [] buffer = new byte[1024];
	//		int len;
	//		while (bufin.available() != 0) {
	//		len = bufin.read(buffer);
	//		sig.update(buffer,0,len); }
	//		bufin.close();
	//		boolean verifies = sig.verify(firma); System.out.println("Verificacion: " + verifies);
	//		}
	//		catch (Exception e) {
	//		System.out.println("Excepcion: " + e.getMessage()); }
	//		}

	public static void main(String[] args) {
		Cliente c = new Cliente();
	}

}
