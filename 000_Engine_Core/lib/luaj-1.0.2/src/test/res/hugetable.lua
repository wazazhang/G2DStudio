-- test tables with more than 50 elements

local t = { 1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  		1,1,1,1,1,1,1,1,1,1,
  }
print ("#t=",#t,'t[1,50,51,59]', t[1], t[50], t[51], t[59])
print (table.concat(t,','))

local t2= {	0,3,4,7,9,8,12,15,23,5,
    	10,13,14,17,19,18,112,115,123,15,
   	20,33,24,27,29,28,212,215,223,25,
  	40,43,44,47,49,48,412,415,423,45,
	50,53,54,57,59,58,512,515,523,55,
	60,63,64,67,69,68,612,615,623,65,
	70,73,74,77,79,78,72,715,723,75,
  }

print ("#t2=",#t2,'t[1,50,51,59]', t[1], t[50], t[51], t[59])
print (table.concat(t2,','))

local t = { 
	[2000]='a',	[2001]='b', [2002]='c', [2003]='d', [2004]='e', [2005]='f', [2006]='g', [2007]='h', [2008]='i', [2009]='j',
	[3000]='a',	[3001]='b', [3002]='c', [3003]='d', [3004]='e', [3005]='f', [3006]='g', [3007]='h', [3008]='i', [3009]='j',
 	[4000]='a',	[4001]='b', [4002]='c', [4003]='d', [4004]='e', [4005]='f', [4006]='g', [4007]='h', [4008]='i', [4009]='j',
 	[5000]='a',	[5001]='b', [5002]='c', [5003]='d', [5004]='e', [5005]='f', [5006]='g', [5007]='h', [5008]='i', [5009]='j',
 	[6000]='a',	[6001]='b', [6002]='c', [6003]='d', [6004]='e', [6005]='f', [6006]='g', [6007]='h', [6008]='i', [6009]='j',
 	[7000]='a',	[7001]='b', [7002]='c', [7003]='d', [7004]='e', [7005]='f', [7006]='g', [7007]='h', [7008]='i', [7009]='j',
 	[8000]='a',	[8001]='b', [8002]='c', [8003]='d', [8004]='e', [8005]='f', [8006]='g', [8007]='h', [8008]='i', [8009]='j',
}

for i=2000,8000,1000 do
	for j=0,9,1 do
		print( 't['..tostring(i+j)..']', t[i+j] )
	end
end
