using AutoMapper.QueryableExtensions;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class TransactionApi
    {
        public IEnumerable<TransactionCustomViewModel> GetTransactionByUserId(string userId)
        {
            var transactions = this.BaseService.GetTransactionByUserId(userId);
            var result = transactions.ProjectTo<TransactionCustomViewModel>(this.AutoMapperConfig).AsEnumerable();
            return result;
        }

        public int CreateTransaction(TransactionViewModel model)
        {
            var entity = model.ToEntity();
            this.BaseService.Create(entity);
            return entity.Id;
        }
    }


}