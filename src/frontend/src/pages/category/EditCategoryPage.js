import React, {Component} from 'react';
import EditCategoryForm from '../../categories/category/EditCategoryForm';
import {notification} from 'antd';
import {editCategory, getCategory} from '../../util/api/APIUtils';
import {Route} from "react-router-dom";
import LoadingIndicator from "../../common/LoadingIndicator";

class EditCategoryPage extends Component {

  state = {
    category: null
  }

  constructor(props) {
    super();

    this.state = {
      categoryId: props.match.params.id,
      loading: true
    };
  }

  componentWillMount() {
    getCategory(this.state.categoryId)
      .then(response => {
        this.setState({
          category: response.data,
          loading: false
        });
        console.log(this.state.category.name)
      })
      .catch(reason => {
        console.log(reason);
        this.setState({loading: false})
      });
  }

  postCategory = (updatedCategory) => {
    editCategory(updatedCategory)
      .then((response) => {
        notification.success({
          message: 'Success',
          description: 'Category edited',
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
      this.state.category ?
        <div className="bc-content-body">
          <h3>Edit Category</h3>
          <Route render={() => <EditCategoryForm
            id={this.state.category.id}
            name={this.state.category.name}
            action={this.postCategory}/>
          }/>
        </div>
        : <LoadingIndicator/>

    )
  }
}

export default EditCategoryPage;