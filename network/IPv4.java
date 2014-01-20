package network;

/**
 * An IPv4 representing class, based on 32 Bit integer, for retrieving some more information.
 * Im Rahmen des Schulunterrichts an der HSS-Wiesloch entstanden.
 * 
 * @author Markus Kwasnicki
 *
 */
public class IPv4 {

	/**
	 * Splits the given IP as human readable string into an representing integer.
	 * 
	 * @param ipAsString The IP representing string.
	 * @exception Exception If the input data is given in an invalid format an exception will be thrown.
	 * @return The IP representing integer.
	 */
	public static int getIpAsIntFromHumanReadableString(String ipAsString) throws Exception
	{
		String ipRegex = new String("^(\\d{1,3}\\.){3}\\d{1,3}$");
		if (!ipAsString.matches(ipRegex)) {
			throw new Exception();
		}

		int octetsAsInt[] = new int[4];
		String octetsAsString[] = new String[4];
		
		octetsAsString = ipAsString.split("\\.");
		for (int i = 0; i < octetsAsString.length; i++) {
			octetsAsInt[i] = Integer.parseInt(octetsAsString[i]);
			if (octetsAsInt[i] > 255) {
				throw new Exception();
			}
		}
		
		int ipAsInt = 0;
		for (int i = 0; i < octetsAsInt.length; i++) {
			if(i > 0)
				ipAsInt = ipAsInt << 8;
			
			ipAsInt = ipAsInt | octetsAsInt[i];
		}
		
		return ipAsInt;
	}
	
	/**
	 * Retrieves a custom IP from integer as human readable string.
	 * 
	 * @param ipAsInt An IP representing integer.
	 * @return The custom IP as human readable string.
	 */
	private static String getIpAsStringFromInt(int ipAsInt)
	{
		int octets[] = getIpAsIntOctets(ipAsInt);
		String ipAsString = new String();
		
		for (int i = 0; i < octets.length - 1; i++) {
			ipAsString += octets[i];
			ipAsString = ipAsString.concat(".");
		}
		ipAsString += octets[3];
		
		return ipAsString;
	}
	
	/**
	 * Splits the given IP as integer into an array containing all octets as integer.
	 * 
	 * @param ipAsInt An IP representing integer.
	 * @return The array containing all IP octets as integer.
	 */
	private static int[] getIpAsIntOctets(int ipAsInt)
	{
		int octets[] = new int[4];
		
		for (int i = 0; i < octets.length; i++) {
			octets[(octets.length - 1) - i] = (ipAsInt >> (8 * i)) & 255;
		}
		
		return octets;
	}
	
	/**
	 * Creates an subnetmask by given subnet bits used.
	 * 
	 * @param subnetBits The number of subnet bits used.
	 * @return The appropriate subnetmask as integer.
	 */
	private static int getSubnetmaskAsInt(int subnetBits)
	{
		int subnetMask = 0;
		
		for (int i = 0; i < subnetBits; i++) {
			if (i > 0) {
				subnetMask = subnetMask >> 1;
			}
			subnetMask = subnetMask | (int)(Math.pow(2, 32) * -1);	// The 32nd bit only set
		}
		
		return subnetMask;
	}
	
	private int ipAsInt;
	private int subnetBits;
	
	/**
	 * Sets the IP address as integer and the number of subnet bits used while instancing.
	 * 
	 * @param ipAsInt The IP representing integer.
	 * @param subnetBits The number of subnet bits used.
	 * @exception Exception If the number of subnet bits is out of valid range an exception will be thrown. 
	 */
	public IPv4(int ipAsInt, int subnetBits) throws Exception
	{
		if(subnetBits < 0 || subnetBits > 32)
			throw new Exception();
		
		this.ipAsInt = ipAsInt;
		this.subnetBits = subnetBits;
	}
	
	/**
	 * Retrieves the instance IP as human readable string.
	 * 
	 * @return The instance IP as human readable string.
	 */
	public String getIpAsString()
	{
		return getIpAsStringFromInt(this.ipAsInt);
	}
	
	/**
	 * Determines the IP class.
	 * 
	 * @return The appropriate IP class (A, B or C) or NULL if unknown.
	 */
	public String getIpClass()
	{
		String ipClass;
		
		if (this.isIpClassA()) {
			ipClass = new String("A");	
		}
		else if (this.isIpClassB()){
			ipClass = new String("B");	
		}
		else if (this.isIpClassC()) {
			ipClass = new String("C");	
		}
		else {
			ipClass = null;	// There are some more classes
		}
		
		return ipClass;
	}
	
	/**
	 * Performs a class A check.
	 * 
	 * @return True if IP is of class A Type and false otherwise.
	 */
	public boolean isIpClassA()
	{
		int octets[] = getIpAsIntOctets(this.ipAsInt);
		
		// Bit 8 = 0 of first octet
		if (octets[0] >> 7 == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Performs a class B check.
	 * 
	 * @return True if IP is of class B Type and false otherwise.
	 */
	public boolean isIpClassB()
	{
		int octets[] = getIpAsIntOctets(this.ipAsInt);
		
		// Bit 8 & 7 = 10 of first octet
		if (octets[0] >> 6 == 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Performs a class C check.
	 * 
	 * @return True if IP is of class C Type and false otherwise.
	 */
	public boolean isIpClassC()
	{
		int octets[] = getIpAsIntOctets(this.ipAsInt);
		
		// Bit 8 & 7 = 11 of first octet
		if (octets[0] >> 6 == 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Determines the IP access method.
	 * 
	 * @return The appropriate IP Public or Private access method.
	 */
	public String getIpAccess()
	{
		String ipAccess;
		int octets[] = getIpAsIntOctets(this.ipAsInt);
		
		if (octets[0] == 10) {
			ipAccess = new String("Private");	// IP starting with 10.XXX.XXX.XXX
		}
		else if (octets[0] == 172 && (octets[1] >= 16 && octets[1] <= 32)) {
			ipAccess = new String("Private");	// IP range from 172.16.XXX.XXX to 172.32.XXX.XXX [including]
		}
		else if ((octets[0] == 192) && (octets[1] == 168)) {
			ipAccess = new String("Private");	// IP starting with 192.168.XXX.XXX 
		}
		else {
			ipAccess = "Public";
		}
		
		return ipAccess;
	}
	
	/**
	 * Retrieves the standard subnetmask for the IP instance.
	 * 
	 * @return The standard subnetmask as representing string.
	 */
	public String getMajorNetmaskAsString()
	{
		return getIpAsStringFromInt(this.getMajorNetmaskAsInt());
	}
	
	/**
	 * Determines the standard subnetmask for the IP instance.
	 * 
	 * @return The standard subnetmask as representing integer.
	 */
	public int getMajorNetmaskAsInt()
	{
		int majorNetmask = 0;
		
		if (this.isIpClassA()) {
			majorNetmask = getSubnetmaskAsInt(this.getNumberOfMajorSubnetBits());
		}
		else if (this.isIpClassB()){
			majorNetmask = getSubnetmaskAsInt(this.getNumberOfMajorSubnetBits());
		}
		else if (this.isIpClassC()) {
			majorNetmask = getSubnetmaskAsInt(this.getNumberOfMajorSubnetBits());
		}
		
		return majorNetmask;
	}
	
	/**
	 * Retrieves the major Net-ID for the IP instance.
	 * 
	 * @return The appropriate Net-ID as representing string.
	 */
	public String getMajorNetIdAsString()
	{
		return getIpAsStringFromInt(this.getMajorNetIdAsInt());
	}

	/**
	 * Determines the major Net-ID for the IP instance.
	 * 
	 * @return The appropriate Net-ID as representing integer.
	 */
	public int getMajorNetIdAsInt()
	{
		return this.ipAsInt & this.getMajorNetmaskAsInt();
	}
	
	/**
	 * Retrieves the major broadcast address as string.
	 * 
	 * @return The appropriate broadcast address as string.
	 */
	public String getMajorBroadcastAddressAsString()
	{
		return getIpAsStringFromInt(this.getMajorBroadcastAddressAsInt());
	}

	/**
	 * Determines the major broadcast address as integer.
	 * 
	 * @return The appropriate broadcast address as integer.
	 */
	public int getMajorBroadcastAddressAsInt()
	{
		return this.getMajorNetIdAsInt() | ~this.getMajorNetmaskAsInt();
	}
	
	/**
	 * Retrieves the appropriate number of major subnet bits used.
	 * 
	 * @return The number of major subnet bits used.
	 */
	public int getNumberOfMajorSubnetBits()
	{
		int majorSubnetBits = 0;
		
		if (this.isIpClassA()) {
			majorSubnetBits = 8;
		}
		else if (this.isIpClassB()){
			majorSubnetBits = 16;
		}
		else if (this.isIpClassC()) {
			majorSubnetBits = 24;
		}
		
		return majorSubnetBits;
	}

	/**
	 * Retrieves the number of subnet bits used.
	 * 
	 * @return The number of subnet bits used.
	 */
	public int getNumberOfSubnetBits()
	{
		return this.subnetBits;
	}

	/**
	 * Retrieves the number of major host bits available.
	 * 
	 * @return The number of major host bits available.
	 */
	public int getNumberOfMajorHostBits()
	{
		return 32 - this.getNumberOfMajorSubnetBits();
	}

	/**
	 * Retrieves the number of host bits available.
	 * 
	 * @return The number of host bits available.
	 */
	public int getNumberOfHostBits()
	{
		return 32 - this.getNumberOfSubnetBits();
	}

	/**
	 * Determines the subnetmask for the IP instance.
	 * 
	 * @return The subnetmask as representing integer.
	 */
	public int getSubnetmaskAsInt()
	{
		return getSubnetmaskAsInt(this.subnetBits);
	}
	
	/**
	 * Retrieves the subnetmask for the IP instance.
	 * 
	 * @return The subnetmask as string.
	 */
	public String getSubnetmaskAsString()
	{
		return getIpAsStringFromInt(this.getSubnetmaskAsInt());
	}
	
	/**
	 * Determines the Subnet-ID for the IP instance.
	 * 
	 * @return The appropriate Subnet-ID as representing integer.
	 */
	public int getSubnetIdAsInt()
	{
		return this.ipAsInt & this.getSubnetmaskAsInt();
	}
	
	/**
	 * Retrieves the Subnet-ID for the IP instance.
	 * 
	 * @return The appropriate Subnet-ID as string.
	 */
	public String getSubnetIdAsString()
	{
		return getIpAsStringFromInt(this.getSubnetIdAsInt());
	}

	/**
	 * Determines the broadcast address for the subnet IP instance.
	 * 
	 * @return The appropriate broadcast address for the subnet IP instance as integer.
	 */
	public int getBroadcastAddressAsInt()
	{
		return this.getSubnetIdAsInt() | ~this.getSubnetmaskAsInt();
	}
	
	/**
	 * Retrieves the broadcast address for the subnet IP instance.
	 * 
	 * @return The appropriate broadcast address for the subnet IP instance as string.
	 */
	public String getBroadcastAddressAsString()
	{
		return getIpAsStringFromInt(this.getBroadcastAddressAsInt());
	}
	
	/**
	 * Determines the number of hosts available in the major subnet.
	 * 
	 * @return The number of hosts available in the major subnet.
	 */
	public int getNumberOfMajorHosts()
	{
		return (int)(Math.pow(2, this.getNumberOfMajorHostBits()) - 2);
	}

	/**
	 * Determines the number of hosts available in the subnet.
	 * 
	 * @return The number of hosts available in the subnet.
	 */
	public long getNumberOfHosts()
	{
		long numberOfHostBits = 0;
		
		if (this.getNumberOfHostBits() > 0) {
			numberOfHostBits = (long)(Math.pow(2, this.getNumberOfHostBits()) - 2);
		}
		
		return numberOfHostBits;
	}

	/**
	 * Calculates the first usable IP address in the major net.
	 * 
	 * @return The first usable IP address in the major net as integer.
	 */
	public int getMajorIpMinAsInt()
	{
		return this.getMajorNetIdAsInt() + 1;
	}
	
	/**
	 * Retrieves the first usable IP address in the major net.
	 * 
	 * @return The first usable IP address in the major net as string.
	 */
	public String getMajorIpMinAsString()
	{
		return getIpAsStringFromInt(this.getMajorIpMinAsInt());
	}
	
	/**
	 * Calculates the last usable IP address in the major net.
	 * 
	 * @return The last usable IP address in the major net as integer.
	 */
	public int getMajorIpMaxAsInt()
	{
		return this.getMajorBroadcastAddressAsInt() - 1;
	}
	
	/**
	 * Retrieves the last usable IP address in the major net.
	 * 
	 * @return The last usable IP address in the major net as string.
	 */
	public String getMajorIpMaxAsString()
	{
		return getIpAsStringFromInt(this.getMajorIpMaxAsInt());
	}
	
	/**
	 * Calculates the first usable IP address in the subnet.
	 * 
	 * @return The first usable IP address in the subnet as integer.
	 */
	public int getIpMinAsInt()
	{
		int ipMin = 0;
		
		if (this.getNumberOfHostBits() > 1) {
			ipMin = this.getSubnetIdAsInt() + 1;
		}
		
		return ipMin; 
	}
	
	/**
	 * Retrieves the first usable IP address in the subnet.
	 * 
	 * @return The first usable IP address in the subnet as string.
	 */
	public String getIpMinAsString()
	{
		return getIpAsStringFromInt(this.getIpMinAsInt());
	}
	
	/**
	 * Calculates the last usable IP address in the subnet.
	 * 
	 * @return The last usable IP address in the subnet as integer.
	 */
	public int getIpMaxAsInt()
	{
		int ipMax = 0;
		
		if (this.getNumberOfHostBits() > 1) {
			ipMax = this.getBroadcastAddressAsInt() - 1;
		}
		
		return ipMax;
	}
	
	/**
	 * Retrieves the last usable IP address in the subnet.
	 * 
	 * @return The last usable IP address in the subnet as string.
	 */
	public String getIpMaxAsString()
	{
		return getIpAsStringFromInt(this.getIpMaxAsInt());
	}
	
	/**
	 * Determines the kind of the IP address instance.
	 * Possible types: NETID, BROADCAST, HOST, N/A
	 * 
	 * @return The kind of the IP address instance as string.
	 */
	public String getKindOfIpAddressAsStringConstant()
	{
		String type = null;
		
		if (this.getNumberOfHostBits() == 0) {
			type = "N/A";
		}
		else if (this.ipAsInt == this.getSubnetIdAsInt()) {
			type = "NETID";
		}
		else if (this.ipAsInt == this.getBroadcastAddressAsInt()) {
			type = "BROADCAST";
		}
		else {
			type ="HOST";
		}
		
		return type;
	}
	
}
