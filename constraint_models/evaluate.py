import sys
import pandas as pd


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Usage: python3 evaluate.py <mode_num>')
        print('\tmode_num should be one of 1 ~ 3')
        exit(1)

    max_val = 3

    try:
        mode = int(sys.argv[1])

        if mode > max_val:
            raise ValueError
    except ValueError:
        print('Usage: python3 evaluate.py <mode_num>')
        print('\tmode_num should be one of 1 ~ 3')
        exit(1)

    data_df = pd.read_csv('./solvable.csv')

    if mode == 1:
        # filter by column values
        data_df = data_df[(data_df['optimization'] == 'None') & (data_df['option'] == 'None')]
        print(data_df.to_string(index=False))

    elif mode == 2:
        # filter by column values
        data_df = data_df[data_df['optimization'] == 'None']
        # group by unique combination of cards and seed
        grouped_df = data_df.groupby(['cards', 'seed'])

        # iterate pandas groupby instance
        for name, df in grouped_df:
            print('\n(cards,seed)={}'.format(name))
            print(df.to_string(index=False))

    elif mode == 3:
        # filter by column values
        data_df = data_df[data_df['option'] == 'None']
        # group by unique combination of cards and seed
        grouped_df = data_df.groupby(['cards', 'seed'])

        # iterate pandas groupby instance
        for name, df in grouped_df:
            print('\n(cards,seed)={}'.format(name))
            print(df.to_string(index=False))
