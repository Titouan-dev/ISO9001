import os

c = []

def compte(path):
	lst_file = os.listdir(path)
	for f in lst_file:
		if os.path.isdir(path + "/" + f):
			compte(path + "/" + f)
		else:
                        reader = open(path + "/" + f, "r")
                        for line in reader:
                                c.append(1)
                        reader.close()
compte("ISO9001")
print(c)		
	
