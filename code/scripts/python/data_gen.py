import datetime
import random

from chinese_calendar import is_workday

# python -m pip install -i https://pypi.tuna.tsinghua.edu.cn/simple chinesecalendar
file_name = './vdata.txt'
file_obj = open(file_name, 'w')
file_obj.truncate()
key1_list = [str]
for i in range(0, 60):
    key1_str = ('A' + ('%d' % i).zfill(4))
    key1_list.append(key1_str)
init_date = datetime.date(2004, 1, 1)
for i in range(0, 2500):
    delta = datetime.timedelta(days=i)
    new_date = init_date + delta
    if is_workday(new_date):  # 只能支持2004到2020年
        for j in range(1, 51):
            if random.randint(0, 6) % 6 != 1:
                key1 = key1_list[j]
                rand_num = random.randint(0, 5)

                if random.randint(0, 9) != 0:
                    value1 = str(round(random.uniform(999, 9999), 4))
                else:
                    value1 = 'null'
                if random.randint(0, 9) != 0:
                    value2 = str(round(random.uniform(99, 9999), 4))
                else:
                    value2 = 'null'
                if random.randint(0, 9) != 0:
                    value3 = str(round(random.uniform(9, 9999), 4))
                else:
                    value3 = 'null'
                if random.randint(0, 9) != 0:
                    value4 = str(round(random.uniform(999, 9999), 4))
                else:
                    value4 = 'null'
                if random.randint(0, 9) != 0:
                    value5 = str(round(random.uniform(0, 9999), 4))
                else:
                    value5 = 'null'

                if rand_num == 0:
                    line = new_date.strftime('%Y-%m-%d') + '\t' + str(key1) + '\t' + 'C' \
                           + '\t' + value1 + '\t' + value2 + '\t' + value3 + '\t' + value4 + '\t' + value5
                elif rand_num == 1:
                    line = new_date.strftime('%Y-%m-%d') + '\t' + str(key1) + '\t' + 'A' \
                           + '\t' + value1 + '\t' + value2 + '\t' + value3 + '\t' + value4 + '\t' + value5
                else:
                    line = new_date.strftime('%Y-%m-%d') + '\t' + str(key1) + '\t' + 'C' \
                           + '\t' + value1 + '\t' + value2 + '\t' + value3 + '\t' + value4 + '\t' + value5
                    line = line + '\n' + new_date.strftime('%Y-%m-%d') + '\t' + str(key1) + '\t' + 'A' \
                           + '\t' + value1 + '\t' + value2 + '\t' + value3 + '\t' + value4 + '\t' + value5
                print(line + '\n')
                file_obj.write(line + '\n')
            else:
                continue
    else:
        pass