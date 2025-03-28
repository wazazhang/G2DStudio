package com.cell.net.io;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 类型和 integer 的映射关系，用于 TransmissionType = TRANSMISSION_TYPE_EXTERNALIZABLE 类型的消息。
 * 此实现中，通过类名的字符串自然排序顺序注册。
 */
public class ExternalizableFactory implements Comparator<Class<?>>
{
//	-----------------------------------------------------------------------------------------------------------
	
	final private TreeSet<Class<?>>			all_types		= new TreeSet<Class<?>>(this);
	final private Map<Integer, Class<?>>	map_id_type		= new HashMap<Integer, Class<?>>();
	final private Map<Class<?>, Integer>	map_type_id		= new HashMap<Class<?>, Integer>();
	
	private MutualMessageCodec mutual_codec;
	
//	-----------------------------------------------------------------------------------------------------------
	
	public ExternalizableFactory() {}

	public ExternalizableFactory(Class<?> ... classes) {
		this(null, classes);
	}
	public ExternalizableFactory(MutualMessageCodec mutual_codec) {
		this(mutual_codec, mutual_codec.getClasses());
	}
	public ExternalizableFactory(MutualMessageCodec mutual_codec, Class<?> ... classes) {
		this.mutual_codec = mutual_codec;
		if (classes.length > 0) {
			for (Class<?> c : classes) {
				registClasses(c);
			}
			syncAll(true);
		}
	}
	
//	-----------------------------------------------------------------------------------------------------------
	
	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		return o1.getCanonicalName().compareTo(o2.getCanonicalName());
	}

	public boolean containsMessage(Class<?> msg_type) {
		return map_type_id.containsKey(msg_type);
	}
	
	public int getMessageType(Message message) {
		return map_type_id.get(message.getClass());
	}
	
	public int getMessageType(Class<?> msg_type) {
		return map_type_id.get(msg_type);
	}
	
	public boolean containsMessageType(Class<?> msg_type) {
		return map_type_id.containsKey(msg_type);
	}
	
	public Class<?> getMessageClass(int msg_type) {
		return map_id_type.get(msg_type);
	}
	
	public Message createMessage(int type) throws InstantiationException, IllegalAccessException   {
		Class<?> ext_type = map_id_type.get(type);
		return (Message)ext_type.newInstance();
	}

	public Map<Integer, Class<?>> getRegistTypes() {
		return new TreeMap<Integer, Class<?>>(map_id_type);
	}
	
//	-----------------------------------------------------------------------------------------------------------
	/**默认BKDR Hash算法，子类可以覆盖此方法实现自己的HASH算法*/
	protected int typeHash(Class<?> str) {
		return HashFunction.bkdrHash(str.getCanonicalName());
	}
	
	/**
	 * 注册所有的类后，记得调用此句
	 */
	protected Map<Integer, Class<?>> syncAll() {
		return syncAll(false) ;
	}
	
	/**
	 * 注册所有的类后，记得调用此句
	 */
	protected Map<Integer, Class<?>> syncAll(boolean verbos) {
		synchronized (all_types) {
//			int index = 1;
			map_id_type.clear();
			map_type_id.clear();
			for (Class<?> cls : all_types) {
				int hash = typeHash(cls);
				if (map_id_type.containsKey(hash)) {
					Class<?> oldc = map_id_type.get(hash);
					throw new RuntimeException("hash conflict: " +
					oldc.getCanonicalName() + " - " + cls.getCanonicalName());
				}
				map_id_type.put(hash, cls);
				map_type_id.put(cls, hash);
				if (!cls.isEnum()) {
					try {
						if (cls.getConstructor() == null) {}
					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						System.err.println("Message : " + e1.getClass() + " : " + e1.getMessage());
					}
				}
//				index ++;
			}
		}
		Map<Integer, Class<?>> regist_types = getRegistTypes();
		if (verbos) {
			for (Entry<Integer, Class<?>> e : regist_types.entrySet()) {
				String info = "Message :";
				info += " (0x" + Long.toHexString((0x100000000L + (long)e.getKey())).substring(1) + ")";
				if (MutualMessage.class.isAssignableFrom(e.getValue())) {
					info += " (Mutual)";
				}
				else if (ExternalizableMessage.class.isAssignableFrom(e.getValue())) {
					info += " (Externalizable)";
				}
				info += " " + e.getValue().getCanonicalName();
				System.out.println(info);
			}
		}
		return regist_types;
	}
	
	/**
	 * 如果该类是ExternalizableMessage，则将类注册到系统，否则查找在此类中定义的类种类。
	 * @param cls
	 */
	public void registClasses(Class<?> cls) {
		synchronized (all_types) {
			if (ExternalizableMessage.class.isAssignableFrom(cls)) {
				all_types.add(cls);
			}
			else if (MutualMessage.class.isAssignableFrom(cls)) {
				all_types.add(cls);
			}
			else if (Message.class.isAssignableFrom(cls)) {
				all_types.add(cls);
			}
			else if (cls.isEnum()) {
				all_types.add(cls);
			}
			for (Class<?> sub : cls.getDeclaredClasses()) {
				registClasses(sub);
			}
		}
	}

//	-----------------------------------------------------------------------------------------------------------

	/**
	 * 负责编解码MutualMessage类型消息
	 * @return
	 */
	public MutualMessageCodec getMutualCodec() {
		return mutual_codec;
	}

//	-----------------------------------------------------------------------------------------------------------

}
