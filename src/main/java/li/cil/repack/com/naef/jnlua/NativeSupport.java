/*
 * $Id: NativeSupport.java 38 2012-01-04 22:44:15Z andre@naef.com $
 * See LICENSE.txt for license terms.
 */

package li.cil.repack.com.naef.jnlua;

/**
 * Loads the JNLua native library.
 * 
 * The class provides and configures a default loader implementation that loads
 * the JNLua native library by means of the <code>System.loadLibrary</code>
 * method. In some situations, you may want to override this behavior. For
 * example, when using JNLua as an OSGi bundle, the native library is loaded by
 * the OSGi runtime. Therefore, the OSGi bundle activator replaces the loader by
 * a no-op implementaion. Note that the loader must be configured before
 * LuaState is accessed.
 */
public final class NativeSupport {
	// -- Static
	private static final NativeSupport INSTANCE = new NativeSupport();

	// -- State
	private Loader loader = new DefaultLoader();

	/**
	 * Returns the instance.
	 * 
	 * @return the instance
	 */
	public static NativeSupport getInstance() {
		return INSTANCE;
	}

	// -- Construction
	/**
	 * Private constructor to prevent external instantiation.
	 */
	private NativeSupport() {
	}

	// -- Properties
	/**
	 * Return the native library loader.
	 * 
	 * @return the loader
	 */
	public Loader getLoader() {
		return loader;
	}

	/**
	 * Sets the native library loader.
	 * 
	 * @param loader
	 *            the loader
	 */
	public void setLoader(Loader loader) {
		if (loader == null) {
			throw new NullPointerException("loader must not be null");
		}
		this.loader = loader;
	}

	// -- Member types
	/**
	 * Loads the library.
	 */
	public interface Loader {
		public LoaderInfo load();
	}

	public interface LoaderInfo {
		int getRegistryIndex();

		String getVersion();
	}

	private class DefaultLoader implements Loader {
		@Override
		public LoaderInfo load() {
			System.loadLibrary("jnlua52");
			return new DefaultLoaderInfo();
		}
	}

	private class DefaultLoaderInfo implements LoaderInfo {
		@Override
		public int getRegistryIndex() {
			return LuaState.lua_registryindex();
		}

		@Override
		public String getVersion() {
			return LuaState.lua_version();
		}
	}
}