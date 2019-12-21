package wcyoung.encrypt.dbcp.tomcat;

import java.util.Base64;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

public class EncryptedBasicDataSourceFactory extends BasicDataSourceFactory {

    private final String ENCRYPTED_PREFIX = "ENC(";
    private final String ENCRYPTED_SUFFIX = ")";

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
            throws Exception {
        if (obj instanceof Reference) {
            findDecryptAndReplace((Reference) obj, "password");
        }

        return super.getObjectInstance(obj, name, nameCtx, environment);
    }

    private void findDecryptAndReplace(Reference reference, String addrType) {
        int index = find(reference, addrType);
        if (index != -1) {
            String decryptedText = decrypt(reference, index);
            replace(reference, addrType, index, decryptedText);
        }
    }

    private int find(Reference reference, String addrType) {
        Enumeration<RefAddr> enumeration = reference.getAll();
        for (int i = 0; enumeration.hasMoreElements(); i++) {
            RefAddr refAddr = enumeration.nextElement();
            if (refAddr.getType().compareTo(addrType) == 0) {
                return i;
            }
        }

        return -1;
    }

    private String decrypt(Reference reference, int index) {
        String original = reference.get(index).getContent().toString();
        if (original.startsWith(ENCRYPTED_PREFIX) && original.endsWith(ENCRYPTED_SUFFIX)) {
            original = original.substring(ENCRYPTED_PREFIX.length());
            original = original.substring(0, original.length() - ENCRYPTED_SUFFIX.length());
            // TODO decrypt
            return new String(Base64.getDecoder().decode(original.getBytes()));
        }

        return original;
    }

    private void replace(Reference reference, String addrType, int index, String newValue) {
        reference.remove(index);
        reference.add(index, new StringRefAddr(addrType, newValue));
    }

}
