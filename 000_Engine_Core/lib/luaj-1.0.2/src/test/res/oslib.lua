-- simple os-library tests
-- these can't really be compared to C meaningfully, 
-- because they are so highly os-dependent.
local lib = "org.luaj.lib.j2se.J2seOsLib"
-- local lib = "org.luaj.lib.OsLib"
print( 'require "'..lib..'"', pcall( require, lib ) )
print( 'os', os ~= nil )
print( 'os.clock()', pcall( os.clock ) )
print( 'os.date()', pcall( os.date ) )
print( 'os.difftime(123000, 21250)', pcall( os.difftime, 123000, 21250 ) )
print( 'os.execute("hostname")', pcall( os.execute, 'hostname' ) )
print( 'os.execute("")', pcall( os.execute, '' ) )
print( 'os.getenv()', pcall( os.getenv ) )
print( 'os.getenv("bogus.key")', pcall( os.getenv, 'bogus.key' ) )
print( 'os.getenv("java.runtime.version")', pcall( os.getenv, 'java.runtime.version' ) )
local s,p = pcall( os.tmpname )
local s,q = pcall( os.tmpname )
print( 'os.tmpname()', s, p )
print( 'os.tmpname()', s, q )
print( 'os.remove(p)', pcall( os.remove, p ) )
print( 'os.rename(p,q)', pcall( os.rename, p, q ) )
local s,f = pcall( io.open, p,"w" )
print( 'io.open', s, f )
print( 'write', pcall( f.write, f, "abcdef 12345" ) )
print( 'close', pcall( f.close, f ) )
print( 'os.rename(p,q)', pcall( os.rename, p, q ) )
print( 'os.remove(q)', pcall( os.remove, q ) )
print( 'os.remove(q)', pcall( os.remove, q ) )
print( 'os.setlocale()', pcall( os.setlocale ) )
print( 'os.setlocale("jp")', pcall( os.setlocale, "jp" ) )
print( 'os.setlocale("us","monetary")', pcall( os.setlocale, "us", "monetary" ) )
print( 'os.setlocale(nil,"all")', pcall( os.setlocale, nil, "all" ) )
print( 'os.setlocale("c")', pcall( os.setlocale, "c" ) )
print( 'os.exit(123)' )
-- print( pcall( os.exit, -123 ) )
print( 'failed to exit' )
