import React, {Component} from 'react';
import {Skeleton} from 'antd';
import Categories from '../../categories/Categories';
import {getAllCategories} from "../../util/api/APIUtils";

class CategoriesPage extends Component {
    state = {
        categories: [],
        loading: true,
    };

    componentWillMount() {
        getAllCategories()
            .then(response => {
                this.setState({
                    categories: response.data,
                    loading: false,
                })
            })
            .catch(reason => {
                console.log(reason);
                this.setState({loading: false,});
            })
    }

    render() {
        return (
            <div className="bc-content-body">
                <Skeleton loading={this.state.loading} active paragraph={1}>
                    <Categories categories={this.state.categories}/>
                </Skeleton>
            </div>
        );
    }
}

export default CategoriesPage;
