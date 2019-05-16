package com.baijimao.xmltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadAssetsXml();
    }

    private void loadAssetsXml() {
        try {
            InputStream inputStream = getAssets().open("students.xml");
            //Log.d(TAG, "loadAssetsXml: " + inputStream.toString());
            parseXML(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            // 设置数据源及编码
            parser.setInput(inputStream, "UTF-8");
            // 获取状态类型
            int eventType = parser.getEventType();
            Student student = null;
            List<Student> students = null;
            // 如果还没有到文档结束
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 文档开始事件，可以进行数据初始化处理
                    case XmlPullParser.START_DOCUMENT:
                        // 实例化集合类
                        students = new ArrayList<>();
                        break;
                    // 开始读取某个标签
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("student")) {
                            // 实例化Student对象
                            student = new Student();
                            student.setId(new Integer(parser.getAttributeValue(null, "id")));
                        } else if (student != null) {
                            if (name.equalsIgnoreCase("name")) {
                                student.setName(parser.nextText());
                            } else if (name.equalsIgnoreCase("age")) {
                                student.setAge(new Integer(parser.nextText()));
                            }
                        }
                        break;
                    // 某个标签解析结束
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("student") && student != null) {
                            Log.d(TAG, "parseXML: student" + student.toString());
                            students.add(student);
                            student = null;
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            inputStream.close();

        } catch (IOException e) {
          e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

}
