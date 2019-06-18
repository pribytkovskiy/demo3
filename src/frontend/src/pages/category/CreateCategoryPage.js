import React, {Component} from 'react';
import CreateCategoryForm from '../../categories/category/CreateCategoryForm';
import {Card, notification} from 'antd';
import {createCategory} from '../../util/api/APIUtils';

class CreateCategoryPage extends Component {

  postCategory = (newCategory) => {
    createCategory(newCategory)
      .then((response) => {
        notification.success({
          message: 'Success',
          description: 'Category created',
        });
        this.props.history.push("/categories");
      })
      .catch((error) => {
        notification.error({
          message: 'Failed',
          description: 'Something went wrong',
        });
        console.log(error);
      });
  };

  render() {
    return (
      <div className="bc-content-body">
        <Card
          title="Create new category"
          bordered={false}>
          <CreateCategoryForm
            currentUser={this.props.currentUser}
            action={this.postCategory}/>
        </Card>
      </div>
    );
  }
}

export default CreateCategoryPage;