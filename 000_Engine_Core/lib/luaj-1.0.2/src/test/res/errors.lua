-- object ids
package.path = "?.lua;src/test/res/?.lua"
require 'ids'

-- test of common types of errors
local function c(f,...) return f(...) end
local function b(...) return c(...) end
local function a(...) return (pcall(b,...)) end
s = 'some string'
local t = {}
-- error message tests
print( 'a(error)', a(error) )
print( 'a(error,"msg")', a(error,"msg") )
print( 'a(error,"msg",0)', a(error,"msg",0) )
print( 'a(error,"msg",1)', a(error,"msg",1) )
print( 'a(error,"msg",2)', a(error,"msg",2) )
print( 'a(error,"msg",3)', a(error,"msg",3) )
print( 'a(error,"msg",4)', a(error,"msg",4) )
print( 'a(error,"msg",5)', a(error,"msg",5) )
print( 'a(error,"msg",6)', a(error,"msg",6) )

-- call errors
print( 'a(nil())', a(function() return n() end) )
print( 'a(t())  ', a(function() return t() end) )
print( 'a(s())  ', a(function() return s() end) )
print( 'a(true())', a(function() local b = true; return b() end) )

-- arithmetic errors
print( 'a(nil+1)', a(function() return nil+1 end) )
print( 'a(a+1)  ', a(function() return a+1 end) )
print( 'a(s+1)  ', a(function() return s+1 end) )
print( 'a(true+1)', a(function() local b = true; return b+1 end) )

-- table errors
print( 'a(nil.x)', a(function() return n.x end) )
print( 'a(a.x)  ', a(function() return a.x end) )
print( 'a(s.x)  ', a(function() return s.x end) )
print( 'a(true.x)', a(function() local b = true; return b.x end) )
print( 'a(nil.x=5)', a(function() n.x=5 end) )
print( 'a(a.x=5)  ', a(function() a.x=5 end) )
print( 'a(s.x=5)  ', a(function() s.x=5 end) )
print( 'a(true.x=5)', a(function() local b = true; b.x=5 end) )

-- len operator 
print( 'a(#nil) ', a(function() return #n end) )
print( 'a(#t)   ', a(function() return #t end) )
print( 'a(#s)   ', a(function() return #s end) )
print( 'a(#a)   ', a(function() return #a end) )
print( 'a(#true)', a(function() local b = true; return #b end) )

-- comparison errors
print( 'a(nil>1)', a(function() return nil>1 end) )
print( 'a(a>1)  ', a(function() return a>1 end) )
print( 'a(s>1)  ', a(function() return s>1 end) )
print( 'a(true>1)', a(function() local b = true; return b>1 end) )

-- unary minus errors
print( 'a(-nil)', a(function() return -n end) )
print( 'a(-a)  ', a(function() return -a end) )
print( 'a(-s)  ', a(function() return -s end) )
print( 'a(-true)', a(function() local b = true; return -b end) )

-- string concatenation
local function concatsuite(comparefunc) 
	print( '"a".."b"', comparefunc("a","b") )
	print( '"a"..nil', comparefunc("a",nil) )
	print( 'nil.."b"', comparefunc(nil,"b") )
	print( '"a"..{}', comparefunc("a",{}) )
	print( '{}.."b"', comparefunc({},"b") )
	print( '"a"..2', comparefunc("a",2) )
	print( '2.."b"', comparefunc(2,"b") )
	print( '"a"..print', comparefunc("a",print) )
	print( 'print.."b"', comparefunc(print,"b") )
	print( '"a"..true', comparefunc("a",true) )
	print( 'true.."b"', comparefunc(true,"b") )
	print( 'nil..true', comparefunc(nil,true) )
	print( '"a"..3.5', comparefunc("a",3.5) )
	print( '3.5.."b"', comparefunc(3.5,"b") )
end
local function strconcat(a,b)
	return (pcall( function() return a..b end) ) 
end
local function tblconcat(a,b) 
	local t={a,b}
	return (pcall( function() return table.concat(t,'-') end ))
end
-- concatsuite(strconcat)
concatsuite(tblconcat)

-- pairs
print( 'a(pairs(nil))', a(function() return id(pairs(nil,{})) end) )
print( 'a(pairs(a))  ', a(function() return id(pairs(a,{})) end) )
print( 'a(pairs(s))  ', a(function() return id(pairs(s,{})) end) )
print( 'a(pairs(t))  ', a(function() return id(pairs(t,{})) end) )
print( 'a(pairs(true))', a(function() local b = true; return id(pairs(b,{})) end) )

-- setmetatable
function sm(...)
	return id(setmetatable(...))
end
print( 'a(setmetatable(nil))', a(function() return sm(nil,{}) end) )
print( 'a(setmetatable(a))  ', a(function() return sm(a,{}) end) )
print( 'a(setmetatable(s))  ', a(function() return sm(s,{}) end) )
print( 'a(setmetatable(true))', a(function() local b = true; return sm(b,{}) end) )
print( 'a(setmetatable(t))  ', a(function() return sm(t,{}) end) )
print( 'a(setmetatable(t*))  ', a(function() return sm(t,{__metatable={}}) end) )
print( 'a(setmetatable(t))  ', a(function() return sm(t,{}) end) )
t = {}
print( 'a(setmetatable(t))  ', a(function() return sm(t,{}) end) )
print( 'a(setmetatable(t*))  ', a(function() return sm(t,{__metatable='some string'}) end) )
print( 'a(setmetatable(t))  ', a(function() return sm(t,{}) end) )
print( 'a(setmetatable(t,nil))  ', a(function() return sm(t,nil) end) )
print( 'a(setmetatable(t))  ', a(function() return sm(t) end) )
print( 'a(setmetatable({},"abc"))  ', a(function() return sm({},'abc') end) )

-- bad args to error!
print( 'error("msg","arg")', a(function() error('some message', 'some bad arg') end) )

-- loadfile, dofile on missing files 
print( 'loadfile("bogus.txt")', a(function() return loadfile("bogus.txt") end) )
print( 'dofile("bogus.txt")', a(function() return dofile("bogus.txt") end) )
