package.path = "?.lua;src/test/errors/?.lua"
require 'args'

-- arg type tests for math library functions
local somenumber = {1,0.75,'-1','-0.25'}
local somepositive = {1,0.75,'2', '2.5'}
local notanumber = {nil,astring,aboolean,afunction,atable,athread}
local nonnumber = {astring,aboolean,afunction,atable}

local singleargfunctions = { 
		'abs', 'acos', 'asin', 'atan', 'cos', 'cosh', 'deg', 'exp', 'floor',
		'rad', 'randomseed', 'sin', 'sinh', 'tan', 'tanh',  'frexp', 
	}
		
local singleargposdomain = { 		 
		'log', 'log10', 'sqrt', 'ceil', 
	}
		
local twoargfunctions = { 
		'atan2', 
	}
	
local twoargsposdomain = { 
	'pow', 'fmod', 
}

-- single argument tests
for i,v in ipairs(singleargfunctions) do
	local funcname = 'math.'..v
	banner(funcname)
	checkallpass(funcname,{somenumber})
	checkallerrors(funcname,{notanumber},'bad argument #1')
end	

-- single argument, positive domain tests
for i,v in ipairs(singleargposdomain) do
	local funcname = 'math.'..v
	banner(funcname)
	checkallpass(funcname,{somepositive})
	checkallerrors(funcname,{notanumber},'bad argument #1')
end	

-- two-argument tests
for i,v in ipairs(twoargfunctions) do
	local funcname = 'math.'..v
	banner(funcname)
	checkallpass(funcname,{somenumber,somenumber})
	checkallerrors(funcname,{},'bad argument #')
	checkallerrors(funcname,{notanumber},'bad argument #')
	checkallerrors(funcname,{notanumber,somenumber},'bad argument #1')
	checkallerrors(funcname,{somenumber},'bad argument #2')
	checkallerrors(funcname,{somenumber,notanumber},'bad argument #2')
end

-- two-argument, positive domain tests
for i,v in ipairs(twoargsposdomain) do
	local funcname = 'math.'..v
	banner(funcname)
	checkallpass(funcname,{somepositive,somenumber})
	checkallerrors(funcname,{},'bad argument #')
	checkallerrors(funcname,{notanumber},'bad argument #')
	checkallerrors(funcname,{notanumber,somenumber},'bad argument #1')
	checkallerrors(funcname,{somenumber},'bad argument #2')
	checkallerrors(funcname,{somenumber,notanumber},'bad argument #2')
end

-- math.max
banner('math.max')
checkallpass('math.max',{somenumber})
checkallpass('math.max',{somenumber,somenumber})
checkallerrors('math.max',{},'bad argument #1')
checkallerrors('math.max',{nonnumber},'bad argument #1')
checkallerrors('math.max',{somenumber,nonnumber},'bad argument #2')

-- math.min
banner('math.min')
checkallpass('math.min',{somenumber})
checkallpass('math.min',{somenumber,somenumber})
checkallerrors('math.min',{},'bad argument #1')
checkallerrors('math.min',{nonnumber},'bad argument #1')
checkallerrors('math.min',{somenumber,nonnumber},'bad argument #2')

-- math.random
local somem = {3,4.5,'6.7'}
local somen = {8,9.10,'12.34'}
local notamn = {astring,aboolean,atable,afunction}
banner('math.random')
checkallpass('math.random',{},true)
checkallpass('math.random',{somem},true)
checkallpass('math.random',{somem,somen},true)
checkallpass('math.random',{{-4,-5.6,'-7','-8.9'},{-1,100,23.45,'-1.23'}},true)
checkallerrors('math.random',{{-4,-5.6,'-7','-8.9'}},'interval is empty')
checkallerrors('math.random',{somen,somem},'interval is empty')
checkallerrors('math.random',{notamn,somen},'bad argument #1')
checkallerrors('math.random',{somem,notamn},'bad argument #2')

-- math.ldexp
local somee = {-3,0,3,9.10,'12.34'}
local notae = {nil,astring,aboolean,atable,afunction}
banner('math.ldexp')
checkallpass('math.ldexp',{somenumber,somee})
checkallerrors('math.ldexp',{},'bad argument')
checkallerrors('math.ldexp',{notanumber},'bad argument')
checkallerrors('math.ldexp',{notanumber,somee},'bad argument #1')
checkallerrors('math.ldexp',{somenumber},'bad argument #2')
checkallerrors('math.ldexp',{somenumber,notae},'bad argument #2')