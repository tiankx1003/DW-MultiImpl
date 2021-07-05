#!/bin/bash
tableau_share="https://www.baidu.com"
path="/tmp/test_argparse.txt"
title="test multi image"
ending=`echo "chrome进入[报表平台]("$tableau_share")查看明细"`
webhook="https://oapi.dingtalk.com/robot/send?access_token=b2d9f3b32************************8fd95b4bfbf4e9"
secret="SEC2c7cf407e56c78205a228*******************9db9266c7901b3072fb26629"
appkey="ding*****************m35w"
appsecret="A3RQtRw-9i*************************3lQ0dhWPbn5tT0O"
media_path="/tmp/jpg/rcl1.png,/tmp/jpg/rcl2.png,/tmp/jpg/rcl3.png,/tmp/jpg/rcl4.png,/tmp/jpg/rcl5.png"
python3 /tmp/send_dingtalk_common_test.py   -p $path            \
                                            -t "$title"         \
                                            -e "$ending"        \
                                            -w $webhook         \
                                            -s $secret          \
                                            -ak $appkey         \
                                            -as $appsecret      \
                                            -m $media_path