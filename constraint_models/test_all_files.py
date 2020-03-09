import os
import sys
import subprocess


def executeSavileRowWithAllParams(savile_row_path='./savilerow', dir_name='solvable', mode=1):
    files_in_dir = os.listdir(dir_name)

    for f in files_in_dir:
        if f.endswith('.param'):
            param_file = dir_name + '/' + f
            savile_row = savile_row_path + '/savilerow'

            if mode == 1:
                subprocess.call([savile_row, "./main.eprime", param_file, "-run-solver"])

            elif mode == 2:
                names = ['.infor', '.solution', '.info', '.minion']

                for name in names:
                    try:
                        os.remove("{}{}".format(param_file, name))
                        print('Remove {}{}'.format(param_file, name))
                    except FileNotFoundError:
                        print("{}{} not found".format(param_file, name))



if __name__ == '__main__':
    if len(sys.argv) < 4:
        print('Usage: python3 test_all_files.py <file_path_of_savile_row> <1 or 2> <1 or 2>')
        exit(1)

    try:
        dir_choice = int(sys.argv[2])
        mode = int(sys.argv[3])
        if mode != 1 and mode != 2:
            raise ValueError
        if dir_choice != 1 and dir_choice != 2:
            raise ValueError
    except ValueError:
        print('Usage: python3 test_all_files.py <file_path_of_savile_row> <1 or 2> <1 or 2>')
        exit(1)

    target = 'solvable' if dir_choice == 1 else 'unsolvable'
    executeSavileRowWithAllParams(sys.argv[1], target, mode)
