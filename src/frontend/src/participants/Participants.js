import React from 'react';
import {List, Avatar, Tag} from 'antd';

const Participants = (props) => {

  return (
    <List
      itemLayout="horizontal"
      dataSource={props.participants}
      renderItem={item => (
        <List.Item>
          <List.Item.Meta
            avatar={<Avatar src="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png"/>}
            title={(item.userId)
              ? <a href={"/user/" + item.userId}>{item.firstName + ' ' + item.lastName}</a>
              : <span>{item.firstName + ' ' + item.lastName}</span>}
            description={<Tag>{item.category.name}</Tag>}
          />
        </List.Item>)
      }
    />
  );
};

export default Participants;
