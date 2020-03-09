import os
import re
import sys


if len(sys.argv) < 2:
    print('Usage : python3 remove.py <file_path>')
file_path = sys.argv[1]
names = ['.infor', '.solution', '.info', '.minion']

for name in names:
    try:
        os.remove("{}{}".format(file_path, name))
        print('Remove {}{}'.format(file_path, name))
    except FileNotFoundError:
        print("{}{} not found".format(file_path, name))

splitted = file_path.split('/')
dir_path = ''

for i in range(len(splitted) - 1):
    dir_path += splitted[i]
    dir_path += '/'

pattern = '*.solution.'

for f in os.listdir(dir_path):
    if '.solution.' in f:
        os.remove('{}{}'.format(dir_path, f))
        print('Remove {}{}'.format(dir_path, f))
