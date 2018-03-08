package t2;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

public class OldRSABreaker {

	private int n;
	private BigInteger e;
	private int encryptedMessage;

	public void setN(int n) {
		this.n = n;
	}

	public void setE(BigInteger e) {
		this.e = e;
	}

	public BigInteger getPhi() {
		Pair p = sieve();
		BigInteger bigP = BigInteger.valueOf(p.getP());
		BigInteger bigQ = BigInteger.valueOf(p.getQ());
		BigInteger bigPminusOne = bigP.subtract(BigInteger.ONE);
		BigInteger bigQminusOne = bigQ.subtract(BigInteger.ONE);
		return bigPminusOne.multiply(bigQminusOne);
	}
	public void setEncryptedMessage(int encryptedMessage) {
		this.encryptedMessage = encryptedMessage;
	}
	static BigInteger[] gcd(BigInteger e2, BigInteger bigInteger) {
	      if (bigInteger.compareTo(BigInteger.ZERO)==0)
	         return new BigInteger[] { e2, BigInteger.ONE, BigInteger.ZERO };

	      BigInteger[] vals = gcd(bigInteger, e2.mod( bigInteger));
	      BigInteger d = vals[0];
	      BigInteger a = vals[2];
	      BigInteger b = vals[1].subtract((e2.divide(bigInteger).multiply(vals[2])));
	      return new BigInteger[] { d, a, b };
	   }
	public BigInteger getD() { 
		BigInteger [] egcd = gcd(e, getPhi());
		return egcd[1];
	}

	public BigInteger decryptMessage() {
		return BigInteger.valueOf(encryptedMessage).pow(getD().intValue()).mod(BigInteger.valueOf(n));
	}

	public Pair sieve() {
		boolean[] isPrime = new boolean[n+1];
		for (int i = 2; i <= n; i++) {
			isPrime[i] = true;
		}

		for (int factor = 2; factor*factor <= n; factor++) {
			if (isPrime[factor]) {
				for (int j = factor; factor*j <= n; j++) {
					isPrime[factor*j] = false;
				}
			}
		}
		
		for (int i = 0; i <= isPrime.length/2; i++) {
			if(isPrime[i]) {
				if(n%i ==0) {
					int j = n/i;
					if(isPrime[j]) {
						return new Pair(i, j);
					}
				}
			}
		}
		return null;

	}
	public static void main(String[] args) {
		OldRSABreaker rsa = new OldRSABreaker();
		rsa.setE(BigInteger.valueOf(94981));
		rsa.setN(352794499);
		rsa.setEncryptedMessage(188885141);
		System.out.println(rsa.decryptMessage());
	}
}