package fi.pyramus.persistence.usertypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class MonetaryAmountUserType implements UserType {

  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) value;
  }

  public boolean equals(Object x, Object y) throws HibernateException {
    if (x == null || y == null)
      return false;
    return x.equals(y);
  }

  public int hashCode(Object x) throws HibernateException {
    return x.hashCode();
  }

  public boolean isMutable() {
    return false;
  }

  public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
    Double valueInEuro = rs.getDouble(names[0]);
    
    if (rs.wasNull())
      return null;
   
    // TODO User's currency
    Currency currency = Currency.getInstance("EUR");
    MonetaryAmount monetaryAmount = new MonetaryAmount(valueInEuro, currency);
    return monetaryAmount.convertTo(currency);
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    if (value == null || ((MonetaryAmount) value).getAmount() == null) {
      st.setNull(index, Hibernate.DOUBLE.sqlType());
    }
    else {
      MonetaryAmount anyCurrency = (MonetaryAmount) value;
      MonetaryAmount amountInEuro = MonetaryAmount.convert(anyCurrency, Currency.getInstance("EUR"));
      st.setDouble(index, amountInEuro.getAmount());
    }
  }

  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }

  @SuppressWarnings("rawtypes")
  public Class returnedClass() {
    return MonetaryAmount.class;
  }

  public int[] sqlTypes() {
    return new int[] { Hibernate.DOUBLE.sqlType() };
  }

}