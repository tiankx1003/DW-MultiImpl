import requests
import json
import time
import hmac
import hashlib
import base64
import urllib.parse
import argparse
import os


def command_argparse():
    """
    获取参数
    -p      --path          存放文本信息的文件路径
    -t      --title         文本信息的标题
    -e      --ending        文本信息的结尾
    -w      --webhook       群机器人webhook
    -s      --secret        群机器人加签
    -ak     --appkey        钉钉程序appkey
    -as     --appsecret     钉钉程序appsecret
    -m      --mediapath     图片路径，多个路径使用逗号分割
    """
    blank = ''
    parser = argparse.ArgumentParser()
    parser.add_argument("-p", "--path", type=str,
                        help="text content file path")
    parser.add_argument("-t", "--title", type=str,
                        help="warning message title")
    parser.add_argument("-e", "--ending", type=str,
                        help="warning message ending")
    parser.add_argument("-w", "--webhook", type=str,
                        help="dingtalk robot webhook link")
    parser.add_argument("-s", "--secret", type=str,
                        help="dingtalk robot secret", default=blank)
    parser.add_argument("-ak", "--appkey", type=str,
                        help="dingtalk application key", default=blank)
    parser.add_argument("-as", "--appsecret", type=str,
                        help="dingtalk application secret", default=blank)
    parser.add_argument("-m", "--mediapath", type=str,
                        help="upload file path", default=blank)
    return parser.parse_args()


def createSign(secret):
    """
    获取签名
    """
    ts_sign = ''
    if secret:
        timestamp = str(round(time.time() * 1000))
        secret_enc = secret.encode('utf-8')
        string_to_sign = '{}\n{}'.format(timestamp, secret)
        string_to_sign_enc = string_to_sign.encode('utf-8')
        hmac_code = hmac.new(secret_enc, string_to_sign_enc,
                             digestmod=hashlib.sha256).digest()
        sign = urllib.parse.quote_plus(base64.b64encode(hmac_code))
        ts_sign = "&timestamp=" + timestamp + "&sign=" + sign
    return ts_sign


def get_content(title, ending, path, media_ids=''):
    """
    获取消息内容
    """
    dt = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    str = '#### **{}**'.format(title)
    for line in open(path, 'r', encoding='utf-8'):
        str = str + "\n\n * " + line
    if media_ids:
        for media_id in media_ids:
            str = str + "\n\n" + "![img]({})".format(media_id)
    content = str + "\n\n > ##### " + ending + "\n\n > ##### " + dt
    return str + "\n\n > ##### " + ending + "\n\n > ##### " + dt


def get_token(appkey, appsecret):
    """
    获取钉钉access_token
    """
    print('appkey=', appkey)
    print('appsecret=', appsecret)
    url = 'https://oapi.dingtalk.com/gettoken?appkey={0}&appsecret={1}'.format(
        appkey, appsecret)
    print(url)
    res = requests.get(url)
    print(res.text)
    access_token = json.loads(res.text)['access_token']
    print('access_token=', access_token)
    return access_token


def upload_media(access_token, media_path, type='file'):
    """上传媒体文件到钉钉"""
    media_ids = []
    for mp in media_path.split(','):
        cmd = """
                curl --location --request POST \
                "https://oapi.dingtalk.com/media/upload?type={2}&access_token={0}" \
                --form 'media=@"{1}"'
                """.format(access_token, mp.strip(), type)
        print('cmd=', cmd)
        res = os.popen(cmd).readline()
        print(res)
        media_id = json.loads(res)['media_id']
        print('media_id=', media_id)
        media_ids.append(media_id)
    print(media_ids)
    return media_ids


def dingTalk(webhook, content, ts_sign='', title='cobweb monitor'):
    """
    发送消息
    """
    headers = {
        "Content-Type": "application/json"
    }
    data = {
        "msgtype": "markdown",
        "markdown": {
            "title": title,
            "text": content
        }
    }
    json_data = json.dumps(data)
    requests.post(url=webhook+ts_sign, data=json_data, headers=headers)


def exit_check():
    print("done!")


def main():
    args = command_argparse()
    if args.appkey:
        access_token = get_token(args.appkey, args.appsecret)
        media_ids = upload_media(access_token, args.mediapath)
    else:
        media_ids = []
    content = get_content(args.title, args.ending, args.path, media_ids)
    ts_sign = createSign(args.secret)
    dingTalk(args.webhook, content, ts_sign, args.title)
    exit_check()


if __name__ == "__main__":
    main()
