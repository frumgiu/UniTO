import java.util.*;
//Per tenere traccia degli identificatori e i loro indirizzi
public class SymbolTable
{

     Map <String, Integer> OffsetMap = new HashMap <String,Integer>();

	public void insert (String s, int address)
    {
            if( !OffsetMap.containsValue(address) ) 
                OffsetMap.put(s,address);
            else 
                throw new IllegalArgumentException("Reference to a memory location already occupied by another variable");
	}
//Ritorna l'indirizzo dell'identificatore
	public int lookupAddress (String s)
    {
            if (OffsetMap.containsKey(s))
                return OffsetMap.get(s);
            else
                return -1;
	}
}
