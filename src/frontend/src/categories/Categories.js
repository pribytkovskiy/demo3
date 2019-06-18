import React, {Component} from 'react';
import {Button, Card, Table} from 'antd';
import {Link} from "react-router-dom";

class Categories extends Component {

  state = {
    categories: {value: this.props.categories}
  };

  render() {
    const categoriesTableColumns = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'id',
        width: 300,
      },
      {
        title: 'Actions',
        key: 'actions',
        render: (text, record) => (
          <span>
            <a href={"/categories/edit/" + record.id}>Edit</a>
          </span>
        ),
        width: 50,
        align: 'center'
      },
    ];

    return (
      <div>
        <Card title={<Link to={"/categories/new"}><Button icon="plus">Add category</Button></Link>}
              bordered={false}>
          <div className="table-wrap">
            <Table columns={categoriesTableColumns}
                   dataSource={this.props.categories}
                   size='small'
                   bordered={true}
                   rowKey={(record) => record.id}
            />
          </div>
        </Card>
      </div>
    );
  }
}

export default Categories;